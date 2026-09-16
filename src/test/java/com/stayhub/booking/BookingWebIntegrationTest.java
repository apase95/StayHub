package com.stayhub.booking;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.stayhub.auth.UserPrincipal;
import com.stayhub.property.Property;
import com.stayhub.property.PropertyRepository;
import com.stayhub.property.PropertyStatus;
import com.stayhub.property.PropertyType;
import com.stayhub.support.PostgreSqlIntegrationTest;
import com.stayhub.user.User;
import com.stayhub.user.UserRepository;
import com.stayhub.user.UserRole;
import com.stayhub.user.UserStatus;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
class BookingWebIntegrationTest extends PostgreSqlIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private UserRepository userRepository;

    private User guest;
    private Property property;
    private UsernamePasswordAuthenticationToken authentication;

    @BeforeEach
    void setUp() {
        bookingRepository.deleteAll();
        propertyRepository.deleteAll();
        userRepository.deleteAll();
        User host = userRepository.save(user("booking-web-host@example.com", UserRole.HOST));
        guest = userRepository.save(user("booking-web-guest@example.com", UserRole.GUEST));
        property = propertyRepository.save(property(host));
        UserPrincipal principal = UserPrincipal.create(guest);
        authentication = new UsernamePasswordAuthenticationToken(
                principal, principal.getPassword(), principal.getAuthorities());
    }

    @AfterEach
    void cleanUp() {
        bookingRepository.deleteAll();
        propertyRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void availabilityApiReturnsSuccessForFreeDates() throws Exception {
        mockMvc.perform(post("/api/v1/bookings/check-availability")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "propertyId": %d,
                                  "checkInDate": "%s",
                                  "checkOutDate": "%s",
                                  "guests": 2
                                }
                                """.formatted(property.getId(), LocalDate.now().plusDays(5), LocalDate.now().plusDays(7))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.available").value(true))
                .andExpect(jsonPath("$.data.priceQuote.totalPrice").value(2300000.00));
    }

    @Test
    void availabilityApiReturnsRoomNotAvailableForConflicts() throws Exception {
        createExistingBooking(LocalDate.now().plusDays(8), LocalDate.now().plusDays(10));

        mockMvc.perform(post("/api/v1/bookings/check-availability")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "propertyId": %d,
                                  "checkInDate": "%s",
                                  "checkOutDate": "%s",
                                  "guests": 2
                                }
                                """.formatted(property.getId(), LocalDate.now().plusDays(9), LocalDate.now().plusDays(11))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").value("ERR_ROOM_NOT_AVAILABLE"));
    }

    @Test
    void mvcBookingFlowCreatesPendingBookingAndShowsPaymentPage() throws Exception {
        mockMvc.perform(get("/properties/{id}/book", property.getId())
                        .param("checkInDate", LocalDate.now().plusDays(12).toString())
                        .param("checkOutDate", LocalDate.now().plusDays(14).toString())
                        .param("guests", "2")
                        .with(authentication(authentication)))
                .andExpect(status().isOk())
                .andExpect(view().name("booking/booking"))
                .andExpect(model().attributeExists("property", "bookingRequest", "priceQuote"));

        mockMvc.perform(post("/bookings")
                        .with(authentication(authentication))
                        .with(csrf())
                        .param("propertyId", property.getId().toString())
                        .param("checkInDate", LocalDate.now().plusDays(12).toString())
                        .param("checkOutDate", LocalDate.now().plusDays(14).toString())
                        .param("guests", "2"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/bookings/*/payment"));

        Booking booking = bookingRepository.findAll().getFirst();
        mockMvc.perform(get("/bookings/{id}/payment", booking.getId())
                        .with(authentication(authentication)))
                .andExpect(status().isOk())
                .andExpect(view().name("booking/payment"))
                .andExpect(model().attributeExists("booking"));
    }

    private void createExistingBooking(LocalDate checkInDate, LocalDate checkOutDate) {
        bookingRepository.save(Booking.builder()
                .property(property)
                .guest(guest)
                .checkInDate(checkInDate)
                .checkOutDate(checkOutDate)
                .guests(2)
                .nightlyPrice(new BigDecimal("1000000.00"))
                .cleaningFee(new BigDecimal("100000.00"))
                .serviceFee(new BigDecimal("200000.00"))
                .totalPrice(new BigDecimal("2300000.00"))
                .status(BookingStatus.PENDING)
                .build());
    }

    private User user(String email, UserRole role) {
        return User.builder()
                .email(email)
                .passwordHash("password-hash")
                .fullName(role.name() + " User")
                .role(role)
                .status(UserStatus.ACTIVE)
                .build();
    }

    private Property property(User host) {
        return Property.builder()
                .host(host)
                .title("Booking Web House")
                .description("A bright stay near the beach.")
                .address("1 Ocean Street")
                .city("Da Nang")
                .pricePerNight(new BigDecimal("1000000.00"))
                .cleaningFee(new BigDecimal("100000.00"))
                .maxGuests(4)
                .bedrooms(2)
                .beds(2)
                .bathrooms(2)
                .propertyType(PropertyType.HOUSE)
                .status(PropertyStatus.ACTIVE)
                .ratingAvg(BigDecimal.ZERO)
                .build();
    }
}

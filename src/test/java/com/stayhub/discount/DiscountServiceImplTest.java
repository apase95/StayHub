package com.stayhub.discount;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.stayhub.booking.BookingPriceService;
import com.stayhub.booking.BookingRepository;
import com.stayhub.common.exception.BusinessException;
import com.stayhub.discount.dto.DiscountValidateRequest;
import com.stayhub.property.Property;
import com.stayhub.property.PropertyRepository;
import com.stayhub.property.PropertyStatus;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DiscountServiceImplTest {
    private static final Long GUEST_ID = 7L;
    private static final Long PROPERTY_ID = 3L;

    @Mock
    private DiscountCodeRepository discountCodeRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private PropertyRepository propertyRepository;

    private DiscountServiceImpl discountService;

    @BeforeEach
    void setUp() {
        discountService = new DiscountServiceImpl(
                discountCodeRepository,
                bookingRepository,
                propertyRepository,
                new BookingPriceService()
        );
    }

    @Test
    void validateAppliesPercentDiscountWithCap() {
        when(propertyRepository.findByIdAndStatus(PROPERTY_ID, PropertyStatus.ACTIVE)).thenReturn(Optional.of(property()));
        when(discountCodeRepository.findByCodeIgnoreCase("SAVE20")).thenReturn(Optional.of(discount("SAVE20", DiscountType.PERCENT, "20.00")));
        when(bookingRepository.countByGuestIdAndDiscountCodeId(GUEST_ID, 11L)).thenReturn(0L);

        var response = discountService.validate(GUEST_ID, request("SAVE20"));

        assertThat(response.getSubtotalPrice()).isEqualByComparingTo("230.00");
        assertThat(response.getDiscountAmount()).isEqualByComparingTo("30.00");
        assertThat(response.getTotalPrice()).isEqualByComparingTo("200.00");
    }

    @Test
    void validateRejectsExpiredDiscount() {
        DiscountCode discount = discount("OLD", DiscountType.FIXED, "10.00");
        discount.setStartsAt(Instant.now().minusSeconds(7200));
        discount.setEndsAt(Instant.now().minusSeconds(3600));
        when(propertyRepository.findByIdAndStatus(PROPERTY_ID, PropertyStatus.ACTIVE)).thenReturn(Optional.of(property()));
        when(discountCodeRepository.findByCodeIgnoreCase("OLD")).thenReturn(Optional.of(discount));

        assertThatThrownBy(() -> discountService.validate(GUEST_ID, request("OLD")))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Discount code is not active.");
    }

    @Test
    void validateRejectsPerUserLimit() {
        when(propertyRepository.findByIdAndStatus(PROPERTY_ID, PropertyStatus.ACTIVE)).thenReturn(Optional.of(property()));
        when(discountCodeRepository.findByCodeIgnoreCase("ONCE")).thenReturn(Optional.of(discount("ONCE", DiscountType.FIXED, "10.00")));
        when(bookingRepository.countByGuestIdAndDiscountCodeId(GUEST_ID, 11L)).thenReturn(1L);

        assertThatThrownBy(() -> discountService.validate(GUEST_ID, request("ONCE")))
                .isInstanceOf(BusinessException.class)
                .hasMessage("You have already used this discount code.");
    }

    private DiscountValidateRequest request(String code) {
        DiscountValidateRequest request = new DiscountValidateRequest();
        request.setPropertyId(PROPERTY_ID);
        request.setCheckInDate(LocalDate.now().plusDays(1));
        request.setCheckOutDate(LocalDate.now().plusDays(3));
        request.setGuests(2);
        request.setCode(code);
        return request;
    }

    private Property property() {
        Property property = Property.builder()
                .pricePerNight(new BigDecimal("100.00"))
                .cleaningFee(new BigDecimal("10.00"))
                .maxGuests(4)
                .status(PropertyStatus.ACTIVE)
                .build();
        property.setId(PROPERTY_ID);
        return property;
    }

    private DiscountCode discount(String code, DiscountType type, String value) {
        DiscountCode discount = DiscountCode.builder()
                .code(code)
                .type(type)
                .value(new BigDecimal(value))
                .cap(new BigDecimal("30.00"))
                .minimumAmount(BigDecimal.ZERO)
                .startsAt(Instant.now().minusSeconds(3600))
                .endsAt(Instant.now().plusSeconds(3600))
                .usageLimit(10)
                .usedCount(0)
                .perUserLimit(1)
                .active(true)
                .build();
        discount.setId(11L);
        return discount;
    }
}

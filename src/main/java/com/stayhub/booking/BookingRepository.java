package com.stayhub.booking;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("""
            select b from Booking b
            join fetch b.property p
            join fetch p.host
            join fetch b.guest
            where p.id = :propertyId
              and b.status in :statuses
              and b.checkInDate < :checkOutDate
              and b.checkOutDate > :checkInDate
            order by b.checkInDate asc
            """)
    List<Booking> findConflictingBookings(@Param("propertyId") Long propertyId,
                                          @Param("checkInDate") LocalDate checkInDate,
                                          @Param("checkOutDate") LocalDate checkOutDate,
                                          @Param("statuses") Collection<BookingStatus> statuses);

    @Query("""
            select b from Booking b
            join fetch b.property p
            join fetch p.host
            join fetch b.guest
            where b.guest.id = :guestId
            order by b.checkInDate desc, b.createdAt desc
            """)
    List<Booking> findByGuestId(@Param("guestId") Long guestId);

    @Query("""
            select b from Booking b
            join fetch b.property p
            join fetch p.host
            join fetch b.guest
            where p.id = :propertyId and b.status = :status
            order by b.checkInDate asc
            """)
    List<Booking> findByPropertyIdAndStatus(@Param("propertyId") Long propertyId,
                                            @Param("status") BookingStatus status);

    @Query("""
            select b from Booking b
            join fetch b.property p
            join fetch p.host
            join fetch b.guest
            where p.host.id = :hostId and b.status = :status
            order by b.createdAt desc
            """)
    List<Booking> findBookingRequestsByHost(@Param("hostId") Long hostId,
                                            @Param("status") BookingStatus status);
}

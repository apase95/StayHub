package com.stayhub.review;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    boolean existsByBookingId(Long bookingId);

    Optional<Review> findByBookingId(Long bookingId);

    @Query("""
            select r from Review r
            join fetch r.guest
            where r.property.id = :propertyId
            order by r.createdAt desc
            """)
    List<Review> findByPropertyIdOrderByCreatedAtDesc(@Param("propertyId") Long propertyId);

    @Query("select coalesce(avg(r.rating), 0) from Review r where r.property.id = :propertyId")
    Double calculateAverageRating(@Param("propertyId") Long propertyId);
}

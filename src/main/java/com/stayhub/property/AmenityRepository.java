package com.stayhub.property;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AmenityRepository extends JpaRepository<Amenity, Long> {
    List<Amenity> findAllByOrderByNameAsc();
}

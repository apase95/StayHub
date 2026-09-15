package com.stayhub.property;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PropertyImageRepository extends JpaRepository<PropertyImage, Long> {

    long countByPropertyId(Long propertyId);

    Optional<PropertyImage> findByIdAndPropertyId(Long id, Long propertyId);

    List<PropertyImage> findAllByPropertyIdOrderByDisplayOrderAsc(Long propertyId);

    Optional<PropertyImage> findFirstByPropertyIdOrderByDisplayOrderAsc(Long propertyId);

    Optional<PropertyImage> findFirstByPropertyIdAndCoverTrue(Long propertyId);

    @Query("select coalesce(max(i.displayOrder), -1) from PropertyImage i where i.property.id = :propertyId")
    int findMaxDisplayOrder(@Param("propertyId") Long propertyId);

    @Modifying(flushAutomatically = true)
    @Query("update PropertyImage i set i.cover = false where i.property.id = :propertyId and i.cover = true")
    void clearCover(@Param("propertyId") Long propertyId);

    @Modifying(flushAutomatically = true)
    @Query("update PropertyImage i set i.displayOrder = :displayOrder where i.id = :id and i.property.id = :propertyId")
    int updateDisplayOrder(@Param("propertyId") Long propertyId,
                           @Param("id") Long id,
                           @Param("displayOrder") int displayOrder);
}

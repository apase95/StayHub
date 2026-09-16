package com.stayhub.property;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;

public interface PropertyRepository extends JpaRepository<Property, Long>, JpaSpecificationExecutor<Property> {

    Optional<Property> findByIdAndStatus(Long id, PropertyStatus status);

    Optional<Property> findByIdAndHostId(Long id, Long hostId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from Property p where p.id = :propertyId and p.host.id = :hostId")
    Optional<Property> findOwnedPropertyForUpdate(@Param("propertyId") Long propertyId,
                                                  @Param("hostId") Long hostId);

    List<Property> findAllByHostIdOrderByUpdatedAtDesc(Long hostId);
}

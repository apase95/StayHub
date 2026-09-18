package com.stayhub.discount;

import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DiscountCodeRepository extends JpaRepository<DiscountCode, Long> {
    Optional<DiscountCode> findByCodeIgnoreCase(String code);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select d from DiscountCode d where upper(d.code) = upper(:code)")
    Optional<DiscountCode> findByCodeIgnoreCaseForUpdate(@Param("code") String code);

    @Modifying
    @Query("update DiscountCode d set d.usedCount = d.usedCount + 1 where d.id = :id")
    int incrementUsedCount(@Param("id") Long id);
}

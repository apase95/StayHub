package com.stayhub.discount.dto;

import com.stayhub.common.validation.DateRangeRequest;
import com.stayhub.common.validation.ValidDateRange;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Data;

@Data
@ValidDateRange
public class DiscountValidateRequest implements DateRangeRequest {
    @NotNull
    private Long propertyId;

    @NotNull
    @FutureOrPresent
    private LocalDate checkInDate;

    @NotNull
    private LocalDate checkOutDate;

    @NotNull
    @Min(1)
    private Integer guests;

    @NotBlank
    private String code;
}

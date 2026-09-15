package com.stayhub.property.dto;

import com.stayhub.property.PropertyStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PropertyUpdateRequest extends PropertyCreateRequest {
    @NotNull
    private PropertyStatus status;
}

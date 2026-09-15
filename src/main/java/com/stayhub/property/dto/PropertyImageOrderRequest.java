package com.stayhub.property.dto;

import jakarta.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class PropertyImageOrderRequest {
    @NotEmpty
    private List<Long> imageIds = new ArrayList<>();
}

package com.stayhub.common.validation;

import java.time.LocalDate;

public interface DateRangeRequest {
    LocalDate getCheckInDate();
    LocalDate getCheckOutDate();
}
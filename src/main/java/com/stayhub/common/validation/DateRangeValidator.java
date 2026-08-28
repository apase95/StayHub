package com.stayhub.common.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class DateRangeValidator implements ConstraintValidator<ValidDateRange, DateRangeRequest> {

    @Override
    public boolean isValid(DateRangeRequest request, ConstraintValidatorContext context) {
        if (request == null) {
            return true; 
        }

        LocalDate checkIn = request.getCheckInDate();
        LocalDate checkOut = request.getCheckOutDate();

        if (checkIn == null || checkOut == null) {
            return true;
        }

        return checkIn.isBefore(checkOut);
    }
}
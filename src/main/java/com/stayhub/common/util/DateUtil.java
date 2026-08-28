package com.stayhub.common.util;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class DateUtil {

    private DateUtil() {}

    
    // Calculates the number of nights
    public static long calculateNights(LocalDate checkIn, LocalDate checkOut) {
        if (checkIn == null || checkOut == null || !checkIn.isBefore(checkOut)) {
            throw new IllegalArgumentException("Invalid date range for calculating nights.");
        }
        return ChronoUnit.DAYS.between(checkIn, checkOut);
    }

    // Checks if two date ranges overlap
    // Logic: A overlaps B if (A.Start < B.End) AND (B.Start > A.end)
    public static boolean isOverlap(LocalDate start1, LocalDate end1, LocalDate start2, LocalDate end2) {
        if (start1 == null || end1 == null || start2 == null || end2 == null) {
            return false;
        }
        return start1.isBefore(end2) && start2.isBefore(end1);
    }
}
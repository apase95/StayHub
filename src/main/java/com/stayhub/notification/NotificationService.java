package com.stayhub.notification;

import com.stayhub.booking.Booking;

public interface NotificationService {
    void bookingStatusChanged(Booking booking);
}

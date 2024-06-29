package com.sgedts.ticketreservation.util;

public class Constants {

    // API Endpoints
    public static final String API_BOOKINGS = "/api/bookings";
    public static final String API_BOOKINGS_BOOK = "/book";

    public static final String API_CONCERTS = "/api/concerts";
    public static final String API_CONCERTS_UPCOMING = "/upcoming";
    public static final String API_CONCERTS_SEARCH = "/search";

    // Error Messages
    public static final String ERROR_USER_NOT_FOUND = "User not found";
    public static final String ERROR_CONCERT_NOT_FOUND = "Concert not found";
    public static final String ERROR_NOT_ENOUGH_TICKETS = "Not enough tickets available";
    public static final String ERROR_BOOKING_NOT_ALLOWED = "Booking is only allowed between ";

    // Redis Keys
    public static final String REDIS_LOCK_KEY_PREFIX = "concert:";
}

package com.sgedts.ticketreservation.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class BookingException extends ResponseStatusException {
    public BookingException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}

package com.sgedts.ticketreservation.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ConcertNotFoundException extends ResponseStatusException {
    public ConcertNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}

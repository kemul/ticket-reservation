package com.sgedts.ticketreservation.controller;

import com.sgedts.ticketreservation.model.Booking;
import com.sgedts.ticketreservation.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping("/book")
    public Booking bookTicket(
            @RequestParam Long userId,
            @RequestParam Long concertId,
            @RequestParam int numberOfTickets) {
        return bookingService.bookTicket(userId, concertId, numberOfTickets);
    }
}

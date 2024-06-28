package com.sgedts.ticketreservation.controller;

import com.sgedts.ticketreservation.model.Booking;
import com.sgedts.ticketreservation.service.BookingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class BookingControllerTest {

    @Mock
    private BookingService bookingService;

    @InjectMocks
    private BookingController bookingController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testBookTicket_Success() throws Exception {
        Booking mockBooking = new Booking();
        when(bookingService.bookTicket(anyLong(), anyLong(), anyInt())).thenReturn(mockBooking);

        Booking booking = bookingController.bookTicket(1L, 2L, 50);
        assertNotNull(booking);
        verify(bookingService, times(1)).bookTicket(1L, 2L, 50);
    }

    @Test
    public void testBookTicket_UserNotFound() throws Exception {
        when(bookingService.bookTicket(anyLong(), anyLong(), anyInt()))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            bookingController.bookTicket(1L, 2L, 50);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("User not found", exception.getReason());
        verify(bookingService, times(1)).bookTicket(1L, 2L, 50);
    }

    @Test
    public void testBookTicket_ConcertNotFound() throws Exception {
        when(bookingService.bookTicket(anyLong(), anyLong(), anyInt()))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Concert not found"));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            bookingController.bookTicket(1L, 2L, 50);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Concert not found", exception.getReason());
        verify(bookingService, times(1)).bookTicket(1L, 2L, 50);
    }

    @Test
    public void testBookTicket_InternalServerError() throws Exception {
        when(bookingService.bookTicket(anyLong(), anyLong(), anyInt()))
                .thenThrow(new RuntimeException("Unexpected error"));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            bookingController.bookTicket(1L, 2L, 50);
        });

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatus());
        assertEquals("An unexpected error occurred", exception.getReason());
        verify(bookingService, times(1)).bookTicket(1L, 2L, 50);
    }
}

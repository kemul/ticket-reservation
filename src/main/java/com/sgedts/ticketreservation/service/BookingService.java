package com.sgedts.ticketreservation.service;

import com.sgedts.ticketreservation.exception.BookingException;
import com.sgedts.ticketreservation.exception.ConcertNotFoundException;
import com.sgedts.ticketreservation.exception.UserNotFoundException;
import com.sgedts.ticketreservation.model.Booking;
import com.sgedts.ticketreservation.model.Concert;
import com.sgedts.ticketreservation.model.User;
import com.sgedts.ticketreservation.repository.BookingRepository;
import com.sgedts.ticketreservation.repository.ConcertRepository;
import com.sgedts.ticketreservation.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

@Service
public class BookingService {

    private static final Logger logger = LoggerFactory.getLogger(BookingService.class);

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ConcertRepository concertRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public Booking bookTicket(Long userId, Long concertId, int numberOfTickets) throws BookingException {
        logger.info("Booking ticket for user: {}, concert: {}", userId, concertId);
        validateUser(userId);
        Concert concert = validateConcert(concertId, numberOfTickets);
        validateBookingTime(concert);

        updateAvailableTickets(concert, numberOfTickets);

        Booking booking = createBooking(userId, concert, numberOfTickets);

        logger.info("Successfully booked ticket for user: {}, concert: {}", userId, concertId);
        return bookingRepository.save(booking);
    }

    private void validateUser(Long userId) throws UserNotFoundException {
        if (!userRepository.existsById(userId)) {
            logger.error("User not found: {}", userId);
            throw new UserNotFoundException("User not found");
        }
    }

    private Concert validateConcert(Long concertId, int numberOfTickets)
            throws ConcertNotFoundException, BookingException {
        Optional<Concert> concertOpt = concertRepository.findById(concertId);
        if (!concertOpt.isPresent()) {
            logger.error("Concert not found: {}", concertId);
            throw new ConcertNotFoundException("Concert not found");
        }
        Concert concert = concertOpt.get();

        if (concert.getAvailableTickets() < numberOfTickets) {
            logger.error("Not enough tickets available for concert: {}, requested: {}", concertId, numberOfTickets);
            throw new BookingException("Not enough tickets available");
        }

        return concert;
    }

    private void validateBookingTime(Concert concert) throws BookingException {
        LocalTime now = LocalTime.now();
        if (now.isBefore(concert.getBookingStartTime()) || now.isAfter(concert.getBookingEndTime())) {
            // logger.error("Booking time is outside allowed window for concert: {}",
            // concert.getConcertId());
            throw new BookingException("Booking is only allowed between " + concert.getBookingStartTime() + " and "
                    + concert.getBookingEndTime());
        }
    }

    private void updateAvailableTickets(Concert concert, int numberOfTickets) {
        concert.setAvailableTickets(concert.getAvailableTickets() - numberOfTickets);
        concertRepository.save(concert);
    }

    private Booking createBooking(Long userId, Concert concert, int numberOfTickets) {
        Booking booking = new Booking();
        booking.setUser(userRepository.findById(userId).get());
        booking.setConcert(concert);
        booking.setNumberOfTickets(numberOfTickets);
        booking.setBookingTime(LocalDateTime.now());
        return booking;
    }
}

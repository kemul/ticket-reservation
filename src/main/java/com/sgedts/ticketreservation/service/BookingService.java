package com.sgedts.ticketreservation.service;

import com.sgedts.ticketreservation.exception.ErrorException;
import com.sgedts.ticketreservation.model.Booking;
import com.sgedts.ticketreservation.model.Concert;
import com.sgedts.ticketreservation.repository.BookingRepository;
import com.sgedts.ticketreservation.repository.ConcertRepository;
import com.sgedts.ticketreservation.repository.UserRepository;
import com.sgedts.ticketreservation.util.Constants;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class BookingService {

    private static final Logger logger = LoggerFactory.getLogger(BookingService.class);

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ConcertRepository concertRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RedissonClient redissonClient;

    @Value("${redis.lock.wait-time}")
    private long waitTime;

    @Value("${redis.lock.lease-time}")
    private long leaseTime;

    @Transactional
    public Booking bookTicket(Long userId, Long concertId, int numberOfTickets) throws ErrorException {
        RLock lock = redissonClient.getLock(Constants.REDIS_LOCK_KEY_PREFIX + concertId);
        try {

            // Wait for x seconds and lease the lock for x seconds (default config 10s)
            if (lock.tryLock(waitTime, leaseTime, TimeUnit.SECONDS)) {
                validateUser(userId);

                // check concert avalability
                Concert concert = validateConcert(concertId, numberOfTickets);

                // validate allowed concert time
                validateBookingTime(concert);

                // validate allowed number of book ticket
                updateAvailableTickets(concert, numberOfTickets);

                // create booking process
                Booking booking = createBooking(userId, concert, numberOfTickets);
                return bookingRepository.save(booking);
            } else {
                throw new ErrorException("Could not acquire lock for booking, please try again.");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ErrorException("Thread was interrupted while trying to acquire lock");
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    private void validateUser(Long userId) throws ErrorException {
        if (!userRepository.existsById(userId)) {
            logger.error("User not found: {}", userId);
            throw new ErrorException("User not found");
        }
    }

    private Concert validateConcert(Long concertId, int numberOfTickets) throws ErrorException {
        Optional<Concert> concertOpt = concertRepository.findById(concertId);
        if (!concertOpt.isPresent()) {
            logger.error("Concert not found: {}", concertId);
            throw new ErrorException("Concert not found");
        }
        Concert concert = concertOpt.get();

        if (concert.getAvailableTickets() < numberOfTickets) {
            logger.error("Not enough tickets available for concert: {}, requested: {}", concertId, numberOfTickets);
            throw new ErrorException("Not enough tickets available");
        }

        return concert;
    }

    private void validateBookingTime(Concert concert) throws ErrorException {
        LocalTime now = LocalTime.now();
        if (now.isBefore(concert.getBookingStartTime()) || now.isAfter(concert.getBookingEndTime())) {
            throw new ErrorException("Booking is only allowed between " + concert.getBookingStartTime() + " and "
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

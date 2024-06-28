package com.sgedts.ticketreservation.repository;

import com.sgedts.ticketreservation.model.Concert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConcertRepository extends JpaRepository<Concert, Long>, CustomConcertRepository {

    @Query("SELECT c FROM Concert c WHERE c.concertDate > current_timestamp AND c.availableTickets > 0")
    List<Concert> findUpcomingConcerts();
}

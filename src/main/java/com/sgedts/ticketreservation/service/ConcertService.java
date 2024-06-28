package com.sgedts.ticketreservation.service;

import com.sgedts.ticketreservation.model.Concert;
import com.sgedts.ticketreservation.repository.ConcertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConcertService {

    @Autowired
    private ConcertRepository concertRepository;

    public List<Concert> getUpcomingConcerts() {
        return concertRepository.findUpcomingConcerts();
    }
}

package com.sgedts.ticketreservation.service;

import com.sgedts.ticketreservation.model.Concert;
import com.sgedts.ticketreservation.repository.ConcertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ConcertService {

    @Autowired
    private ConcertRepository concertRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public List<Concert> getUpcomingConcerts() {
        return concertRepository.findUpcomingConcerts();
    }

    public List<Concert> searchConcerts(String search, String date) {
        System.out.println("[Service]search " + search);
        return concertRepository.searchConcerts(search, date);
    }

}

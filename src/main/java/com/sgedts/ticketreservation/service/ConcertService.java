package com.sgedts.ticketreservation.service;

import com.sgedts.ticketreservation.model.Concert;
import com.sgedts.ticketreservation.repository.ConcertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.sgedts.ticketreservation.exception.ErrorException;

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
        List<Concert> lConcerts = concertRepository.findUpcomingConcerts();
        if (lConcerts.size() == 0) {
            throw new ErrorException("Concert not found");
        }
        return lConcerts;
    }

    public List<Concert> searchConcerts(String search, String date) {
        List<Concert> lConcerts = concertRepository.searchConcerts(search, date);
        if (lConcerts.size() == 0) {
            throw new ErrorException("Concert not found");
        }
        return lConcerts;
    }

}

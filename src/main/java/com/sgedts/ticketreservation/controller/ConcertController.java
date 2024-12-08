package com.sgedts.ticketreservation.controller;

import com.sgedts.ticketreservation.exception.ErrorException;
import com.sgedts.ticketreservation.model.Concert;
import com.sgedts.ticketreservation.service.ConcertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import com.sgedts.ticketreservation.util.Constants;

import java.util.List;

@RestController
@RequestMapping(Constants.API_CONCERTS)
public class ConcertController {

    @Autowired
    private ConcertService concertService;

    @GetMapping(Constants.API_CONCERTS_UPCOMING)
    public List<Concert> getUpcomingConcerts() {
        List<Concert> lConcerts = concertService.getUpcomingConcerts();
        if (lConcerts.isEmpty()) {
            throw new ErrorException(Constants.ERROR_CONCERT_NOT_FOUND);
        }
        return lConcerts;
    }

    @GetMapping(Constants.API_CONCERTS_SEARCH)
    public List<Concert> searchConcerts(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String date) {
        return concertService.searchConcerts(search, date);
    }
}

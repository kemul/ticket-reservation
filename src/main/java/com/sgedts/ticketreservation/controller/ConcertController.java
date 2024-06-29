package com.sgedts.ticketreservation.controller;

import com.sgedts.ticketreservation.model.Concert;
import com.sgedts.ticketreservation.service.ConcertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

// API to get available concert 
// Currently handle static query
@RestController
@RequestMapping("/api/concerts")
public class ConcertController {

    @Autowired
    private ConcertService concertService;

    @GetMapping("/upcoming")
    public List<Concert> getUpcomingConcerts() {
        return concertService.getUpcomingConcerts();
    }

    @GetMapping("/search")
    public List<Concert> searchConcerts(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String date) {
        return concertService.searchConcerts(search, date);
    }
}

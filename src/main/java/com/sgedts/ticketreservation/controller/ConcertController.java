package com.sgedts.ticketreservation.controller;

import com.sgedts.ticketreservation.model.Concert;
import com.sgedts.ticketreservation.service.ConcertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// API to get available concert , TODO AddParam
// Currently handle static query, TODO AddDynamicQuery
@RestController
@RequestMapping("/api/concerts")
public class ConcertController {

    @Autowired
    private ConcertService concertService;

    @GetMapping("/upcoming")
    public List<Concert> getUpcomingConcerts() {
        System.out.println("Upcoming Concert.....");
        return concertService.getUpcomingConcerts();
    }
}

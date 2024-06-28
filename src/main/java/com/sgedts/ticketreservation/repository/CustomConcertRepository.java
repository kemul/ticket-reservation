package com.sgedts.ticketreservation.repository;

import com.sgedts.ticketreservation.model.Concert;

import java.util.List;

public interface CustomConcertRepository {
    List<Concert> searchConcerts(String search, String date);
}

package com.sgedts.ticketreservation.controller;

import com.sgedts.ticketreservation.model.Concert;
import com.sgedts.ticketreservation.service.ConcertService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ConcertControllerTest {

    @Mock
    private ConcertService concertService;

    @InjectMocks
    private ConcertController concertController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(concertController).build();
    }

    @Test
    public void testGetUpcomingConcerts() throws Exception {
        Concert concert1 = new Concert();
        concert1.setConcertName("Concert 1");
        Concert concert2 = new Concert();
        concert2.setConcertName("Concert 2");

        List<Concert> concerts = Arrays.asList(concert1, concert2);
        when(concertService.getUpcomingConcerts()).thenReturn(concerts);

        mockMvc.perform(get("/api/concerts/upcoming")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].concertName").value("Concert 1"))
                .andExpect(jsonPath("$[1].concertName").value("Concert 2"));
    }

    @Test
    public void testSearchConcerts() throws Exception {
        Concert concert1 = new Concert();
        concert1.setConcertName("Concert 1");

        List<Concert> concerts = Arrays.asList(concert1);
        when(concertService.searchConcerts("Concert 1", "2024-07-20")).thenReturn(concerts);

        mockMvc.perform(get("/api/concerts/search")
                .param("search", "Concert 1")
                .param("date", "2024-07-20")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].concertName").value("Concert 1"));
    }
}

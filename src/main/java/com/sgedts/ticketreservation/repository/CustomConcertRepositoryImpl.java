package com.sgedts.ticketreservation.repository;

import com.sgedts.ticketreservation.model.Concert;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CustomConcertRepositoryImpl implements CustomConcertRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Concert> searchConcerts(String search, String date) {
        System.out.println("[Repository]search " + search);
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Concert> cq = cb.createQuery(Concert.class);
        Root<Concert> concert = cq.from(Concert.class);

        List<Predicate> predicates = new ArrayList<>();

        // Search by concert name
        if (search != null && !search.isEmpty()) {
            Predicate titlePredicate = cb.like(concert.get("concertName"), "%" + search + "%");
            predicates.add(cb.or(titlePredicate));
        }

        // Filter by date
        if (date != null && !date.isEmpty()) {
            LocalDate localDate = LocalDate.parse(date);
            predicates.add(cb.equal(concert.get("concertDate").as(LocalDate.class), localDate));
        }

        // Filter by available tickets and concert date
        predicates.add(cb.greaterThan(concert.get("availableTickets"), 0));
        predicates.add(cb.greaterThan(concert.get("concertDate"), cb.currentTimestamp()));

        cq.where(predicates.toArray(new Predicate[0]));

        return entityManager.createQuery(cq).getResultList();
    }
}

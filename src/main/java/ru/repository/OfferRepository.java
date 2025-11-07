package ru.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ru.entity.Offer;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Long> {

    @Query("""
       select (count(o) > 0)
       from Offer o join o.application a
       where a.candidate.id = :candidateId
         and o.status = 'ACCEPTED'
       """)
    boolean existsAcceptedForCandidate(@Param("candidateId") Long candidateId);
}

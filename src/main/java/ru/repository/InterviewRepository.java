package ru.repository;

import java.time.Instant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ru.entity.Interview;

@Repository
public interface InterviewRepository extends JpaRepository<Interview, Long> {

    @Query("""
        select count(i)
        from Interview i
        where i.interviewerId = :interviewerId
          and (:excludeId is null or i.id <> :excludeId)
          and i.startTime < :endTime
          and i.endTime   > :startTime
        """)
    long countOverlaps(@Param("interviewerId") Long interviewerId,
                       @Param("startTime") Instant startTime,
                       @Param("endTime") Instant endTime,
                       @Param("excludeId") Long excludeId);
}

package ru.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.entity.Application;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {

    List<Application> findByCandidate_Id(Long candidateId);
    List<Application> findByVacancy_Id(Long vacancyId);

    boolean existsByCandidate_IdAndVacancy_Id(Long candidateId, Long vacancyId);
}

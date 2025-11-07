package ru.service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import ru.DTO.ApplicationDTO;
import ru.entity.Application;
import ru.entity.Candidate;
import ru.entity.Vacancy;
import ru.repository.ApplicationRepository;
import ru.repository.CandidateRepository;
import ru.repository.VacancyRepository;

@Service
@Transactional
public class ApplicationService {

    private final ApplicationRepository appRepo;
    private final CandidateRepository candRepo;
    private final VacancyRepository vacRepo;

    public ApplicationService(ApplicationRepository appRepo, CandidateRepository candRepo, VacancyRepository vacRepo) {
        this.appRepo = appRepo;
        this.candRepo = candRepo;
        this.vacRepo = vacRepo;
    }

    public ApplicationDTO create(ApplicationDTO d) {
        Candidate c = candRepo.findById(d.getCandidateId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Candidate " + d.getCandidateId() + " not found"));
        Vacancy v = vacRepo.findById(d.getVacancyId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vacancy " + d.getVacancyId() + " not found"));

        if (appRepo.existsByCandidate_IdAndVacancy_Id(c.getId(), v.getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Application already exists for candidate " + c.getId() + " and vacancy " + v.getId());
        }

        Application e = new Application();
        e.setCandidate(c);
        e.setVacancy(v);
        e.setStatus(d.getStatus() != null ? d.getStatus() : "CREATED");
        e.setCreatedAt(Instant.now());
        e.setUpdatedAt(Instant.now());
        return toDTO(appRepo.save(e));
    }

    @Transactional(readOnly = true)
    public List<ApplicationDTO> getAll() {
        return appRepo.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ApplicationDTO getById(Long id) {
        Application e = appRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Application " + id + " not found"));
        return toDTO(e);
    }

    public ApplicationDTO update(Long id, ApplicationDTO d) {
        Application e = appRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Application " + id + " not found"));

        Candidate c = candRepo.findById(d.getCandidateId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Candidate " + d.getCandidateId() + " not found"));
        Vacancy v = vacRepo.findById(d.getVacancyId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vacancy " + d.getVacancyId() + " not found"));

        if (!e.getCandidate().getId().equals(c.getId()) || !e.getVacancy().getId().equals(v.getId())) {
            if (appRepo.existsByCandidate_IdAndVacancy_Id(c.getId(), v.getId())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Application already exists for candidate " + c.getId() + " and vacancy " + v.getId());
            }
        }

        e.setCandidate(c);
        e.setVacancy(v);
        e.setStatus(d.getStatus());
        e.setUpdatedAt(Instant.now());
        return toDTO(appRepo.save(e));
    }

    public void delete(Long id) {
        if (!appRepo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Application " + id + " not found");
        }
        appRepo.deleteById(id);
    }


    private ApplicationDTO toDTO(Application e) {
        return new ApplicationDTO(
                e.getId(),
                e.getCandidate().getId(),
                e.getVacancy().getId(),
                e.getStatus()
        );
    }
}

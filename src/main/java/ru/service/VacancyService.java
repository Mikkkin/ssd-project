package ru.service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import ru.DTO.VacancyDTO;
import ru.entity.Vacancy;
import ru.repository.VacancyRepository;

@Service
@Transactional
public class VacancyService {

    private final VacancyRepository repo;

    public VacancyService(VacancyRepository repo) {
        this.repo = repo;
    }

    public VacancyDTO create(VacancyDTO d) {

        Vacancy e = toEntity(d);
        e.setId(null);
        e.setCreatedAt(Instant.now());
        e.setUpdatedAt(Instant.now());
        if (e.getStatus() == null) e.setStatus("DRAFT");
        return toDTO(repo.save(e));
    }

    public VacancyDTO close(Long id) {

        Vacancy e = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vacancy " + id + " not found"));
        
        if ("CLOSED".equals(e.getStatus())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Vacancy " + id + " is already CLOSED");
        }

        e.setStatus("CLOSED");
        e.setUpdatedAt(Instant.now());
        return toDTO(repo.save(e));
    }

    @Transactional(readOnly = true)
    public List<VacancyDTO> getAll() {
        return repo.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public VacancyDTO getById(Long id) {
        Vacancy e = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vacancy " + id + " not found"));
        return toDTO(e);
    }

    public VacancyDTO update(Long id, VacancyDTO d) {
        Vacancy e = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vacancy " + id + " not found"));
        e.setTitle(d.getTitle());
        e.setDescription(d.getDescription());
        e.setDepartment(d.getDepartment());
        e.setSalaryFrom(d.getSalaryFrom());
        e.setSalaryTo(d.getSalaryTo());
        e.setStatus(d.getStatus());
        e.setUpdatedAt(Instant.now());
        return toDTO(repo.save(e));
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Vacancy " + id + " not found");
        repo.deleteById(id);
    }


    public VacancyDTO publish(Long id) {
        Vacancy v = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vacancy " + id + " not found"));
        if (!"DRAFT".equals(v.getStatus())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Vacancy " + id + " cannot be published from " + v.getStatus());
        }
        v.setStatus("PUBLISHED");
        v.setUpdatedAt(Instant.now());
        return toDTO(repo.save(v));
    }


    private VacancyDTO toDTO(Vacancy e) {
        return new VacancyDTO(
                e.getId(), e.getTitle(), e.getDescription(), e.getDepartment(),
                e.getSalaryFrom(), e.getSalaryTo(), e.getStatus()
        );
    }

    private Vacancy toEntity(VacancyDTO d) {
        Vacancy e = new Vacancy();
        e.setId(d.getId());
        e.setTitle(d.getTitle());
        e.setDescription(d.getDescription());
        e.setDepartment(d.getDepartment());
        e.setSalaryFrom(d.getSalaryFrom());
        e.setSalaryTo(d.getSalaryTo());
        e.setStatus(d.getStatus());
        return e;
    }
}

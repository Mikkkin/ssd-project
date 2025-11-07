package ru.service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import ru.DTO.CandidateDTO;
import ru.entity.Candidate;
import ru.repository.CandidateRepository;

@Service
@Transactional
public class CandidateService {

    private final CandidateRepository repo;

    public CandidateService(CandidateRepository repo) {
        this.repo = repo;
    }

    public CandidateDTO create(CandidateDTO d) {
        // простая защита на уровне сервиса (в БД у тебя ещё и unique индекс по email)
        if (repo.existsByEmail(d.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists: " + d.getEmail());
        }
        Candidate e = toEntity(d);
        e.setId(null);
        e.setCreatedAt(Instant.now());
        e.setUpdatedAt(Instant.now());
        try {
            return toDTO(repo.save(e));
        } catch (DataIntegrityViolationException ex) {
            // если уникальность поймалась именно БД
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists: " + d.getEmail(), ex);
        }
    }

    @Transactional(readOnly = true)
    public List<CandidateDTO> getAll() {
        return repo.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CandidateDTO getById(Long id) {
        Candidate e = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Candidate " + id + " not found"));
        return toDTO(e);
    }

    public CandidateDTO update(Long id, CandidateDTO d) {
        Candidate e = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Candidate " + id + " not found"));
        // если меняется email — проверим коллизию
        if (!e.getEmail().equals(d.getEmail()) && repo.existsByEmail(d.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists: " + d.getEmail());
        }
        e.setFirstName(d.getFirstName());
        e.setLastName(d.getLastName());
        e.setEmail(d.getEmail());
        e.setPhone(d.getPhone());
        e.setPositionWanted(d.getPositionWanted());
        e.setUpdatedAt(Instant.now());
        return toDTO(repo.save(e));
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Candidate " + id + " not found");
        }
        repo.deleteById(id);
    }


    private CandidateDTO toDTO(Candidate e) {
        return new CandidateDTO(
                e.getId(),
                e.getFirstName(),
                e.getLastName(),
                e.getEmail(),
                e.getPhone(),
                e.getPositionWanted()
        );
    }

    private Candidate toEntity(CandidateDTO d) {
        Candidate e = new Candidate();
        e.setId(d.getId());
        e.setFirstName(d.getFirstName());
        e.setLastName(d.getLastName());
        e.setEmail(d.getEmail());
        e.setPhone(d.getPhone());
        e.setPositionWanted(d.getPositionWanted());
        return e;
    }
}

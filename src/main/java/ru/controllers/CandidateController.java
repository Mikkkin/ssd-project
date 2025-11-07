package ru.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import ru.DTO.CandidateDTO;
import ru.service.CandidateService;

@RestController
@RequestMapping("/api/candidates")
public class CandidateController {

    private final CandidateService candidateService;
    public CandidateController(CandidateService candidateService) { this.candidateService = candidateService; }

    @PostMapping
    public ResponseEntity<CandidateDTO> create(@Valid @RequestBody CandidateDTO dto) {
        return new ResponseEntity<>(candidateService.create(dto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CandidateDTO>> getAll() { return ResponseEntity.ok(candidateService.getAll()); }

    @GetMapping("/{id}")
    public ResponseEntity<CandidateDTO> getById(@PathVariable Long id) { return ResponseEntity.ok(candidateService.getById(id)); }

    @PutMapping("/{id}")
    public ResponseEntity<CandidateDTO> update(@PathVariable Long id, @Valid @RequestBody CandidateDTO dto) {
        return ResponseEntity.ok(candidateService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        candidateService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

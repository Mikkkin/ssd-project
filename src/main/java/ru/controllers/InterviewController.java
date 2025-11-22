package ru.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import ru.DTO.InterviewDTO;
import ru.service.InterviewService;


@RestController
@RequestMapping("/api/interviews")
@PreAuthorize("hasAnyRole('HR', 'ADMIN')")
public class InterviewController {

    private final InterviewService interviewService;
    public InterviewController(InterviewService interviewService) { this.interviewService = interviewService; }


    @PostMapping
    @PreAuthorize("hasAnyRole('HR', 'ADMIN')")
    public ResponseEntity<InterviewDTO> create(@Valid @RequestBody InterviewDTO dto) {
        return new ResponseEntity<>(interviewService.create(dto), HttpStatus.CREATED);
    }

    
    @GetMapping
    @PreAuthorize("hasAnyRole('HR', 'ADMIN')")
    public ResponseEntity<List<InterviewDTO>> getAll() { return ResponseEntity.ok(interviewService.getAll()); }


    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('HR', 'ADMIN')")
    public ResponseEntity<InterviewDTO> getById(@PathVariable Long id) { return ResponseEntity.ok(interviewService.getById(id)); }


    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('HR', 'ADMIN')")
    public ResponseEntity<InterviewDTO> update(@PathVariable Long id, @Valid @RequestBody InterviewDTO dto) {
        return ResponseEntity.ok(interviewService.update(id, dto));
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        interviewService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

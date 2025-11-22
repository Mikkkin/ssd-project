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
import ru.DTO.ApplicationDTO;
import ru.service.ApplicationService;


@RestController
@RequestMapping("/api/applications")
@PreAuthorize("hasAnyRole('HR', 'ADMIN')")
public class ApplicationController {

    private final ApplicationService applicationService;
    public ApplicationController(ApplicationService applicationService) { this.applicationService = applicationService; }


    @PostMapping
    @PreAuthorize("hasAnyRole('HR', 'ADMIN', 'CANDIDATE')")
    public ResponseEntity<ApplicationDTO> create(@Valid @RequestBody ApplicationDTO dto) {
        return new ResponseEntity<>(applicationService.create(dto), HttpStatus.CREATED);
    }


    @GetMapping
    @PreAuthorize("hasAnyRole('HR', 'ADMIN')")
    public ResponseEntity<List<ApplicationDTO>> getAll() {
        return ResponseEntity.ok(applicationService.getAll());
    }


    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('HR', 'ADMIN', 'CANDIDATE')")
    public ResponseEntity<ApplicationDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(applicationService.getById(id));
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('HR', 'ADMIN')")
    public ResponseEntity<ApplicationDTO> update(@PathVariable Long id, @Valid @RequestBody ApplicationDTO dto) {
        return ResponseEntity.ok(applicationService.update(id, dto));
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        applicationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

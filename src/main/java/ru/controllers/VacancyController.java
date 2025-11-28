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
import ru.DTO.VacancyDTO;
import ru.service.VacancyService;


@RestController
@RequestMapping("/api/vacancies")
@PreAuthorize("hasAnyRole('HR', 'ADMIN')")
public class VacancyController {

    private final VacancyService vacancyService;

    public VacancyController(VacancyService vacancyService) {
        this.vacancyService = vacancyService;
    }


    @PostMapping
    @PreAuthorize("hasAnyRole('HR', 'ADMIN')")
    public ResponseEntity<VacancyDTO> create(@Valid @RequestBody VacancyDTO dto) {
        return new ResponseEntity<>(vacancyService.create(dto), HttpStatus.CREATED);
    }


    @GetMapping
    @PreAuthorize("hasAnyRole('HR', 'ADMIN')")
    public ResponseEntity<List<VacancyDTO>> getAll() {
        return ResponseEntity.ok(vacancyService.getAll());
    }


    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('HR', 'ADMIN')")
    public ResponseEntity<VacancyDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(vacancyService.getById(id));
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('HR', 'ADMIN')")
    public ResponseEntity<VacancyDTO> update(@PathVariable Long id, @Valid @RequestBody VacancyDTO dto) {
        return ResponseEntity.ok(vacancyService.update(id, dto));
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        vacancyService.delete(id);
        return ResponseEntity.noContent().build();
    }


    @PostMapping("/{id}/publish")
    @PreAuthorize("hasAnyRole('HR', 'ADMIN')")
    public ResponseEntity<VacancyDTO> publish(@PathVariable Long id) {
        return ResponseEntity.ok(vacancyService.publish(id));
    }


    @PostMapping("{id}/close")
    @PreAuthorize("hasAnyRole('HR', 'ADMIN')")
    public ResponseEntity<VacancyDTO> close(@PathVariable Long id) {
        return ResponseEntity.ok(vacancyService.close(id));
    }
        
    
}

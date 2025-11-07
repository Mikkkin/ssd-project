package ru.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.DTO.VacancyDTO;
import ru.service.VacancyService;

import java.util.List;

@RestController
@RequestMapping("/api/vacancies")
public class VacancyController {

    private final VacancyService vacancyService;

    public VacancyController(VacancyService vacancyService) {
        this.vacancyService = vacancyService;
    }

    @PostMapping
    public ResponseEntity<VacancyDTO> create(@Valid @RequestBody VacancyDTO dto) {
        return new ResponseEntity<>(vacancyService.create(dto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<VacancyDTO>> getAll() {
        return ResponseEntity.ok(vacancyService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VacancyDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(vacancyService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VacancyDTO> update(@PathVariable Long id, @Valid @RequestBody VacancyDTO dto) {
        return ResponseEntity.ok(vacancyService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        vacancyService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/publish")
    public ResponseEntity<VacancyDTO> publish(@PathVariable Long id) {
        return ResponseEntity.ok(vacancyService.publish(id));
    }
}

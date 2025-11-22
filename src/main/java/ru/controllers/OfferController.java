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
import ru.DTO.OfferDTO;
import ru.service.OfferService;


@RestController
@RequestMapping("/api/offers")
@PreAuthorize("hasAnyRole('HR', 'ADMIN')")
public class OfferController {

    private final OfferService offerService;

    public OfferController(OfferService offerService) {
        this.offerService = offerService;
    }


    @PostMapping
    @PreAuthorize("hasAnyRole('HR', 'ADMIN')")
    public ResponseEntity<OfferDTO> create(@Valid @RequestBody OfferDTO dto) {
        return new ResponseEntity<>(offerService.create(dto), HttpStatus.CREATED);
    }


    @GetMapping
    @PreAuthorize("hasAnyRole('HR', 'ADMIN')")
    public ResponseEntity<List<OfferDTO>> getAll() {
        return ResponseEntity.ok(offerService.getAll());
    }


    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('HR', 'ADMIN')")
    public ResponseEntity<OfferDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(offerService.getById(id));
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('HR', 'ADMIN')")
    public ResponseEntity<OfferDTO> update(@PathVariable Long id, @Valid @RequestBody OfferDTO dto) {
        return ResponseEntity.ok(offerService.update(id, dto));
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        offerService.delete(id);
        return ResponseEntity.noContent().build();
    }


    @PostMapping("/{id}/accept")
    @PreAuthorize("hasAnyRole('HR', 'ADMIN')")
    public ResponseEntity<OfferDTO> accept(@PathVariable Long id) {
        return ResponseEntity.ok(offerService.accept(id));
    }


    @PostMapping("/{id}/reject")
    @PreAuthorize("hasAnyRole('HR', 'ADMIN')")
    public ResponseEntity<OfferDTO> reject(@PathVariable Long id) {
        return ResponseEntity.ok(offerService.reject(id));
    }
}

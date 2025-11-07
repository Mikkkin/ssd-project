package ru.service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import ru.DTO.OfferDTO;
import ru.entity.Application;
import ru.entity.Offer;
import ru.repository.ApplicationRepository;
import ru.repository.OfferRepository;

@Service
@Transactional
public class OfferService {

    private final OfferRepository offerRepo;
    private final ApplicationRepository appRepo;

    public OfferService(OfferRepository offerRepo, ApplicationRepository appRepo) {
        this.offerRepo = offerRepo;
        this.appRepo = appRepo;
    }

    public OfferDTO create(OfferDTO d) {
        Application app = appRepo.findById(d.getApplicationId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Application " + d.getApplicationId() + " not found"));

        Offer e = new Offer();
        e.setApplication(app);
        e.setCompensation(d.getCompensation());
        e.setStatus(d.getStatus() != null ? d.getStatus() : "SENT");
        e.setCreatedAt(Instant.now());
        e.setUpdatedAt(Instant.now());
        try {
            return toDTO(offerRepo.save(e));
        } catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Offer save failed due to DB constraint", ex);
        }
    }

    @Transactional(readOnly = true)
    public List<OfferDTO> getAll() {
        return offerRepo.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public OfferDTO getById(Long id) {
        Offer e = offerRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Offer " + id + " not found"));
        return toDTO(e);
    }

    public OfferDTO update(Long id, OfferDTO d) {
        Offer e = offerRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Offer " + id + " not found"));
        Application app = appRepo.findById(d.getApplicationId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Application " + d.getApplicationId() + " not found"));

        e.setApplication(app);
        e.setCompensation(d.getCompensation());
        e.setStatus(d.getStatus());
        e.setUpdatedAt(Instant.now());
        return toDTO(offerRepo.save(e));
    }

    public void delete(Long id) {
        if (!offerRepo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Offer " + id + " not found");
        }
        offerRepo.deleteById(id);
    }

    /** БО: принять оффер — запрет второго ACCEPTED для одного кандидата */
    public OfferDTO accept(Long id) {
        Offer e = offerRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Offer " + id + " not found"));
        Long candidateId = e.getApplication().getCandidate().getId();
        if (offerRepo.existsAcceptedForCandidate(candidateId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Candidate " + candidateId + " already has an ACCEPTED offer");
        }
        e.setStatus("ACCEPTED");
        e.setUpdatedAt(Instant.now());
        return toDTO(offerRepo.save(e));
    }

    public OfferDTO reject(Long id) {
        Offer e = offerRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Offer " + id + " not found"));
        e.setStatus("REJECTED");
        e.setUpdatedAt(Instant.now());
        return toDTO(offerRepo.save(e));
    }


    private OfferDTO toDTO(Offer e) {
        return new OfferDTO(
                e.getId(),
                e.getApplication().getId(),
                e.getCompensation(),
                e.getStatus()
        );
    }
}

package ru.service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import ru.DTO.InterviewDTO;
import ru.entity.Application;
import ru.entity.Interview;
import ru.entity.Offer;
import ru.repository.ApplicationRepository;
import ru.repository.InterviewRepository;
import ru.repository.OfferRepository;

@Service
@Transactional
public class InterviewService {

    private final InterviewRepository repo;
    private final ApplicationRepository appRepo;
    private final OfferRepository offerRepo;

    public InterviewService(InterviewRepository repo, ApplicationRepository appRepo, OfferRepository offerRepo) {
        this.repo = repo;
        this.appRepo = appRepo;
        this.offerRepo = offerRepo;
    }

    public InterviewDTO create(InterviewDTO d) {
        if (d.getStartTime() == null || d.getEndTime() == null || !d.getStartTime().isBefore(d.getEndTime())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "startTime must be before endTime");
        }

        Application app = appRepo.findById(d.getApplicationId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Application " + d.getApplicationId() + " not found"));

        long conflicts = repo.countOverlaps(d.getInterviewerId(), d.getStartTime(), d.getEndTime(), null);
        if (conflicts > 0) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Interviewer " + d.getInterviewerId() + " has overlapping interview slot");
        }

        Interview e = new Interview();
        e.setApplication(app);
        e.setInterviewerId(d.getInterviewerId());
        e.setStartTime(d.getStartTime());
        e.setEndTime(d.getEndTime());
        e.setLocation(d.getLocation());
        e.setStatus(d.getStatus() != null ? d.getStatus() : "SCHEDULED");
        e.setCreatedAt(Instant.now());
        e.setUpdatedAt(Instant.now());

        return toDTO(repo.save(e));
    }

    @Transactional(readOnly = true)
    public List<InterviewDTO> getAll() {
        return repo.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public InterviewDTO getById(Long id) {
        Interview e = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Interview " + id + " not found"));
        return toDTO(e);
    }

    public InterviewDTO update(Long id, InterviewDTO d) {
        Interview e = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Interview " + id + " not found"));

        if (d.getStartTime() == null || d.getEndTime() == null || !d.getStartTime().isBefore(d.getEndTime())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "startTime must be before endTime");
        }

        Application app = appRepo.findById(d.getApplicationId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Application " + d.getApplicationId() + " not found"));

        long conflicts = repo.countOverlaps(d.getInterviewerId(), d.getStartTime(), d.getEndTime(), id);
        if (conflicts > 0) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Interviewer " + d.getInterviewerId() + " has overlapping interview slot");
        }

        e.setApplication(app);
        e.setInterviewerId(d.getInterviewerId());
        e.setStartTime(d.getStartTime());
        e.setEndTime(d.getEndTime());
        e.setLocation(d.getLocation());
        e.setStatus(d.getStatus());
        e.setUpdatedAt(Instant.now());

        return toDTO(repo.save(e));
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Interview " + id + " not found");
        }
        repo.deleteById(id);
    }

    /** БО: завершить интервью; при SUCCESS создать Offer (одна транзакция) */
    public InterviewDTO complete(Long id, boolean success) {
        Interview i = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Interview " + id + " not found"));

        i.setStatus("COMPLETED");
        i.setUpdatedAt(Instant.now());
        repo.save(i);

        if (success) {
            Application app = i.getApplication();
            app.setStatus("OFFERED");
            app.setUpdatedAt(Instant.now());
            appRepo.save(app);

            Offer o = new Offer();
            o.setApplication(i.getApplication());
            o.setStatus("SENT");
            o.setCreatedAt(Instant.now());
            o.setUpdatedAt(Instant.now());
            offerRepo.save(o);
        }
        return toDTO(i);
    }


    private InterviewDTO toDTO(Interview e) {
        return new InterviewDTO(
                e.getId(),
                e.getApplication().getId(),
                e.getInterviewerId(),
                e.getStartTime(),
                e.getEndTime(),
                e.getLocation(),
                e.getStatus()
        );
    }
}

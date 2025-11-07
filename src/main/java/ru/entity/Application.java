package ru.entity;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
    name = "applications",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_app_candidate_vacancy", columnNames = {"candidate_id","vacancy_id"})
    },
    indexes = {
        @Index(name = "ix_app_candidate", columnList = "candidate_id"),
        @Index(name = "ix_app_vacancy",  columnList = "vacancy_id")
    }
)
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "candidate_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_app_candidate")
    )
    private Candidate candidate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "vacancy_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_app_vacancy")
    )
    private Vacancy vacancy;

    @Column(nullable = false, length = 32)
    private String status; // CREATED | IN_REVIEW | INTERVIEW_SCHEDULED | REJECTED | OFFERED

    @Column(nullable = false)
    private Instant createdAt = Instant.now();

    @Column(nullable = false)
    private Instant updatedAt = Instant.now();
}

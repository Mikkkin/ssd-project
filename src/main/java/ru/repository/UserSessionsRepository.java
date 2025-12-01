package ru.repository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ru.entity.SessionStatus;
import ru.entity.UserSession;

@Repository
public interface UserSessionsRepository extends JpaRepository<UserSession, UUID> {
    Optional<UserSession> findByRefreshToken(String refreshToken);
    long countByUserEmailAndStatusAndRefreshTokenExpiryAfter(String email, SessionStatus status, Instant now);


    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE UserSession s SET s.status = :status WHERE s.refreshToken = :token")
    int updateStatusByRefreshToken(@Param("token") String token, @Param("status") SessionStatus status);


    @Query("""
        SELECT s FROM UserSession s
        WHERE s.refreshToken = :token
          AND s.status = ru.entity.SessionStatus.ACTIVE
          AND s.refreshTokenExpiry > :now
        """)
    Optional<UserSession> findActiveNotExpired(@Param("token") String token, @Param("now") Instant now);
}

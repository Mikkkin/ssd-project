package ru.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ru.entity.SessionStatus;
import ru.repository.UserSessionsRepository;


@Service
@RequiredArgsConstructor
public class SessionAdminService {

    private final UserSessionsRepository sessionRepository;


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void revokeByToken(String refreshToken) {
        sessionRepository.updateStatusByRefreshToken(refreshToken, SessionStatus.REVOKED);
    }
}


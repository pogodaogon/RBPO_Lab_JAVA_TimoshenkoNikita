package ru.mfa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mfa.model.UserSession;

import java.util.Optional;
import java.util.UUID;

public interface UserSessionRepository extends JpaRepository<UserSession, UUID> {
    Optional<UserSession> findByRefreshToken(String refreshToken);
}

package ru.mfa.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mfa.dto.TokenResponseDto;
import ru.mfa.model.SessionStatus;
import ru.mfa.model.UserSession;
import ru.mfa.repository.UserSessionRepository;
import ru.mfa.security.JwtTokenProvider;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtTokenProvider tokenProvider;
    private final UserSessionRepository sessionRepository;
    private final UserDetailsService userDetailsService;

    @Transactional
    public TokenResponseDto createTokensAndSession(UserDetails userDetails, String deviceId) {
        String accessToken = tokenProvider.generateAccessToken(userDetails, deviceId);
        String refreshToken = tokenProvider.generateRefreshToken(userDetails, deviceId);

        UserSession session = UserSession.builder()
                .username(userDetails.getUsername())
                .deviceId(deviceId)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .accessTokenExpiry(Instant.now().plusMillis(900000)) // 15 mins
                .refreshTokenExpiry(Instant.now().plusMillis(604800000)) // 7 days
                .status(SessionStatus.ACTIVE)
                .build();

        sessionRepository.save(session);

        return new TokenResponseDto(accessToken, refreshToken);
    }

    @Transactional
    public TokenResponseDto refreshTokens(String refreshToken) {
        if (!tokenProvider.validateToken(refreshToken)) {
            throw new BadCredentialsException("Invalid refresh token");
        }

        Optional<UserSession> sessionOpt = sessionRepository.findByRefreshToken(refreshToken);
        if (sessionOpt.isEmpty()) {
            throw new BadCredentialsException("Session not found");
        }

        UserSession session = sessionOpt.get();

        if (session.getStatus() == SessionStatus.USED) {
            // Повторное использование!
            session.setStatus(SessionStatus.REVOKED);
            sessionRepository.save(session);
            throw new BadCredentialsException("Token reuse detected");
        }

        if (session.getStatus() != SessionStatus.ACTIVE) {
            throw new BadCredentialsException("Session is not active");
        }

        if (session.getRefreshTokenExpiry().isBefore(Instant.now())) {
            session.setStatus(SessionStatus.REVOKED);
            sessionRepository.save(session);
            throw new BadCredentialsException("Refresh token expired");
        }

        // Помечаем старую сессию как использованную
        session.setStatus(SessionStatus.USED);
        sessionRepository.save(session);

        // Создаем новые токены
        String username = tokenProvider.getUsernameFromJWT(refreshToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        return createTokensAndSession(userDetails, session.getDeviceId());
    }
}

package ru.mfa.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@jakarta.persistence.Table(name = "user_sessions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSession {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    private String username; // идентификатор пользователя
    
    private String deviceId; // идентификатор устройства или сессии
    
    @Column(length = 512)
    private String accessToken; 
    
    @Column(length = 512, unique = true)
    private String refreshToken; 
    
    private Instant accessTokenExpiry;
    
    private Instant refreshTokenExpiry;
    
    @Enumerated(EnumType.STRING)
    private SessionStatus status; 
}

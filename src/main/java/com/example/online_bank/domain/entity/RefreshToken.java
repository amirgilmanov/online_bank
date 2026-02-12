package com.example.online_bank.domain.entity;

import com.example.online_bank.enums.TokenStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import static jakarta.persistence.EnumType.STRING;

@Entity
@Table
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String tokenHash;

    @Column
    private LocalDateTime expiresAt;

    @Column
    private LocalDateTime revokedAt;

    @Column
    private LocalDateTime createdAt;

    @Column
    @Enumerated(STRING)
    private TokenStatus status;

    @Column
    private String uuidHash;

    @ManyToOne()
    @JoinColumn(name = "family_id")
    private TokenFamily family;
}

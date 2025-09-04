package com.example.online_bank.domain.entity;

import com.example.online_bank.enums.VerifiedCodeType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VerifiedCode {
        @Id
        @GeneratedValue(strategy = GenerationType.UUID)
        private UUID id;

        @Column
        private String verifiedCode;

        @Column
        private LocalDateTime createdAt;

        @Column
        private LocalDateTime expiresAt;

        @Column
        private Boolean isVerified;

        @Column
        @Enumerated(EnumType.STRING)
        private VerifiedCodeType codeType;

        @ManyToOne()
        @JoinColumn(name = "user_id", referencedColumnName = "id")
        @ToString.Exclude
        private User user;
}

package com.example.online_bank.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "auth_users")
public class AuthUsers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String pinCode;

    @JoinColumn(unique = true, nullable = false, name = "user_id", referencedColumnName = "id")
    @OneToOne()
    @ToString.Exclude
    private User user;
}

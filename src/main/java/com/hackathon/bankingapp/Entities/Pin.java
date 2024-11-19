package com.hackathon.bankingapp.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "pins")
public class Pin {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String hashedPin;

    @OneToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = true)
    private LocalDateTime lastUpdated;

    public void updatePin(String newHashedPin) {
        this.hashedPin = newHashedPin;
        this.lastUpdated = LocalDateTime.now();
    }
}
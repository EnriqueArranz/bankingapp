package com.hackathon.bankingapp.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Otp {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String identifier;
    private String otp;
    private LocalDateTime timestamp;
    private String resetToken;

    public Otp(String identifier, String otp, LocalDateTime timestamp) {
        this.identifier = identifier;
        this.otp = otp;
        this.timestamp = timestamp;
    }
}
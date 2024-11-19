package com.hackathon.bankingapp.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private Double amount;
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;
    private LocalDateTime transactionDateTime;
    private String sourceAccountNumber;
    private String targetAccountNumber;

    @Column(nullable = false)
    private String assetSymbol;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;
    private Double units;
    private Double pricePerUnit;
}
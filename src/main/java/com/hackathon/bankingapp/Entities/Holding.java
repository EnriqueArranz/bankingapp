package com.hackathon.bankingapp.Entities;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
public class Holding {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String symbol;

    @Column(nullable = false)
    private Double totalUnits;

    @Column(nullable = false)
    private Double averageCostBasis;

    @Column(nullable = false)
    private Double totalInvestment;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
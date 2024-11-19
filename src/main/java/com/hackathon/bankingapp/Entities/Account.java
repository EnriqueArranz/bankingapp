package com.hackathon.bankingapp.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String accountNumber;
    private double balance;
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private Pin pin;
}


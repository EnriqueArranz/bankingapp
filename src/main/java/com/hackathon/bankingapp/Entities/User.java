package com.hackathon.bankingapp.Entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor

@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;


    private String email;

    private String phoneNumber;

    private String name;

    private String address;

    private String hashedPassword;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "user", orphanRemoval = true)
    private Account account;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Holding> holdings;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> transactions;
}


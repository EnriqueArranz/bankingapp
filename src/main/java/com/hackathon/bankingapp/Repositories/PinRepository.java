package com.hackathon.bankingapp.Repositories;

import com.hackathon.bankingapp.Entities.Account;
import com.hackathon.bankingapp.Entities.Pin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PinRepository extends JpaRepository<Pin, UUID> {
    Optional<Pin> findByAccount(Account account);
}
package com.hackathon.bankingapp.Repositories;

import com.hackathon.bankingapp.Entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findBySourceAccountNumber(String accountNumber);
}
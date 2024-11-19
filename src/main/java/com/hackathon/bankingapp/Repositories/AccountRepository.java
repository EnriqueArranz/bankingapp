package com.hackathon.bankingapp.Repositories;



import com.hackathon.bankingapp.Entities.Account;
import com.hackathon.bankingapp.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {
    Optional<Object> findByUser(User user);

    boolean existsByAccountNumber(String targetAccountNumber);

    Account findByAccountNumber(String targetAccountNumber);
}
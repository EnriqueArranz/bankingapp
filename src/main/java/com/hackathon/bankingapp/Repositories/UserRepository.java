package com.hackathon.bankingapp.Repositories;


import com.hackathon.bankingapp.Entities.User;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    boolean existsByPhoneNumber(@NotBlank(message = "Phone number is required") String phoneNumber);
}

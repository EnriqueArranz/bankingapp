package com.hackathon.bankingapp.Repositories;

import com.hackathon.bankingapp.Entities.Otp;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OtpRepository extends JpaRepository<Otp, Long> {

    Optional<Otp> findByIdentifierAndOtp(String identifier, String otp);

    @Modifying
    @Transactional
    @Query("UPDATE Otp o SET o.resetToken = :resetToken WHERE o.identifier = :identifier")
    void saveResetToken(String identifier, String resetToken);

    @Query("SELECT CASE WHEN COUNT(o) > 0 THEN TRUE ELSE FALSE END FROM Otp o WHERE o.identifier = :identifier AND o.resetToken = :resetToken")
    boolean verifyResetToken(String identifier, String resetToken);

    @Modifying
    @Transactional
    @Query("DELETE FROM Otp o WHERE o.identifier = :identifier")
    void invalidateResetToken(String identifier);

    @Query("SELECT o FROM Otp o WHERE o.identifier = :identifier")
    Optional<Otp> findByIdentifier(String identifier);
}
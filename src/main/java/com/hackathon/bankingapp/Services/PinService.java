package com.hackathon.bankingapp.Services;

import com.hackathon.bankingapp.DTO.PinCreateRequest;
import com.hackathon.bankingapp.DTO.PinCreateResponse;
import com.hackathon.bankingapp.DTO.PinUpdateRequest;
import com.hackathon.bankingapp.DTO.PinUpdateResponse;
import com.hackathon.bankingapp.Entities.Account;
import com.hackathon.bankingapp.Entities.Pin;
import com.hackathon.bankingapp.Entities.User;
import com.hackathon.bankingapp.Exceptions.CustomException;
import com.hackathon.bankingapp.Repositories.AccountRepository;
import com.hackathon.bankingapp.Repositories.PinRepository;
import com.hackathon.bankingapp.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PinService {

    @Autowired
    private PinRepository pinRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }


    private Account getCurrentUserAccount() {
        User user = getCurrentUser();
        return user.getAccount();
    }

    @Transactional
    public PinCreateResponse createPin(PinCreateRequest request) {
        Account account = getCurrentUserAccount();
        String plainPin = request.getPin();
        String password = request.getPassword();
        if (!passwordEncoder.matches(password, account.getUser().getHashedPassword())) {
            throw new CustomException("Invalid password", HttpStatus.BAD_REQUEST);
        }

        if (account.getPin() != null) {
            throw new CustomException("PIN already exists for this account",HttpStatus.BAD_REQUEST);
        }

        String hashedPin = passwordEncoder.encode(plainPin);
        Pin newPin = new Pin();
        newPin.setHashedPin(hashedPin);
        newPin.setAccount(account);
        pinRepository.save(newPin);
        accountRepository.save(account);
        userRepository.save(account.getUser());
        return new PinCreateResponse("PIN created successfully");
    }

    @Transactional
    public PinUpdateResponse updatePin(PinUpdateRequest request) {
        String oldPin = request.getOldPin();
        String password = request.getPassword();
        String newPin = request.getNewPin();
        Account account = getCurrentUserAccount();

        Pin pin = account.getPin();
        if (pin == null) {
            throw new IllegalStateException("No PIN found for this account");
        }

        if (!passwordEncoder.matches(password, account.getUser().getHashedPassword()) ||
                !passwordEncoder.matches(oldPin, pin.getHashedPin())) {
            throw new IllegalArgumentException("Invalid old PIN or password");
        }

        if (passwordEncoder.matches(newPin, pin.getHashedPin())) {
            throw new IllegalArgumentException("New PIN must be different from old PIN");
        }
        String hashedNewPin = passwordEncoder.encode(newPin);
        pin.updatePin(hashedNewPin);
        pinRepository.save(pin);
        accountRepository.save(account);
        return new PinUpdateResponse("PIN updated successfully");
    }
}
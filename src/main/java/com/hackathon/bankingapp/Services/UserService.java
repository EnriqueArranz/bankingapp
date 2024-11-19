package com.hackathon.bankingapp.Services;

import com.hackathon.bankingapp.DTO.*;
import com.hackathon.bankingapp.Entities.Account;
import com.hackathon.bankingapp.Entities.User;
import com.hackathon.bankingapp.Exceptions.CustomException;
import com.hackathon.bankingapp.Repositories.AccountRepository;
import com.hackathon.bankingapp.Repositories.UserRepository;
import com.hackathon.bankingapp.Security.JwtTokenProvider;
import com.hackathon.bankingapp.Security.JwtTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private JwtTokenService jwtTokenService;
    @Autowired
    private final AuthenticationManager authenticationManager;

    @Transactional
    public UserResponse registerUser(UserRegistrationRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new CustomException("Email already exists", HttpStatus.BAD_REQUEST);
        }
        if (userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new CustomException("Phone number already exists", HttpStatus.BAD_REQUEST);
        }
        validateRegistrationDto(request);
        User user = new User();
        Account account = new Account();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setAddress(request.getAddress());
        user.setHashedPassword(passwordEncoder.encode(request.getPassword()));
        user.setAccount(account);
        account.setBalance(0.0);
        account.setAccountNumber(UUID.randomUUID().toString().substring(0, 6));
        account.setUser(user);
        user.setAccount(account);
        userRepository.save(user);
        return new UserResponse(
                user.getName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getAddress(),
                user.getAccount().getAccountNumber().toString().substring(0, 6),
                user.getHashedPassword());
    }

    private void validateRegistrationDto(UserRegistrationRequest dto) {
        if (dto.getName() == null || dto.getName().isEmpty()) {
            throw new CustomException("Name cannot be empty", HttpStatus.BAD_REQUEST);
        }
        if (dto.getEmail() == null || !isValidEmail(dto.getEmail())) {
            throw new CustomException("Invalid email format", HttpStatus.BAD_REQUEST);
        }
        if (dto.getPhoneNumber() == null || dto.getPhoneNumber().isEmpty()) {
            throw new CustomException("Phone number cannot be empty", HttpStatus.BAD_REQUEST);
        }
        if (dto.getAddress() == null || dto.getAddress().isEmpty()) {
            throw new CustomException("Address cannot be empty", HttpStatus.BAD_REQUEST);
        }
        if (dto.getPassword() == null || !isValidPassword(dto.getPassword())) {
            throw new CustomException(getPasswordErrorMessage(dto.getPassword()), HttpStatus.BAD_REQUEST);
        }
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$"; // Basic email regex
        return email.matches(emailRegex);
    }

    private boolean isValidPassword(String password) {
        // Reglas de validación de contraseña
        if (password.length() < 8) {
            return false;
        }
        if (password.length() > 128) {
            return false;
        }
        if (!password.matches(".*[A-Z].*")) {
            return false;
        }
        if (!password.matches(".*\\d.*")) {
            return false;
        }
        if (!password.matches(".*[^a-zA-Z0-9].*")) {
            return false;
        }
        if (password.contains(" ")) {
            return false;
        }
        return true;
    }

    private String getPasswordErrorMessage(String password) {
        // Mensajes de error para cada regla de validación
        if (password.length() < 8) {
            return "Password must be at least 8 characters long";
        }
        if (password.length() > 128) {
            return "Password must be less than 128 characters long";
        }
        if (!password.matches(".*[A-Z].*")) {
            return "Password must contain at least one uppercase letter";
        }
        if (!password.matches(".*\\d.*")) {
            return "Password must contain at least one digit";
        }
        if (!password.matches(".*[^a-zA-Z0-9].*")) {
            return "Password must contain at least one special character";
        }
        if (password.contains(" ")) {
            return "Password cannot contain whitespace";
        }
        return "";
    }

    private String generateAccountNumber() {
        return UUID.randomUUID().toString().substring(0, 6); // Shortened version of UUID
    }

    @Transactional
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getIdentifier())
                .orElseThrow(() -> new CustomException("User not found for the given identifier: " + request.getIdentifier(), HttpStatus.BAD_REQUEST));
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getIdentifier(), request.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new CustomException("Bad credentials", HttpStatus.UNAUTHORIZED);
        }
        // Generate JWT token for the authenticated user
        String token = jwtTokenProvider.generateToken(user.getEmail());
        // Return the token in the login response
        return new LoginResponse(token);
    }

    public User getCurrentUserInfo() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public Account getCurrentAccountInfo() {
        return getCurrentUserInfo().getAccount();
    }

    @Transactional
    public void logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String token = jwtTokenProvider.getTokenFromAuthentication(authentication);
        jwtTokenService.blacklistToken(token);
        SecurityContextHolder.clearContext();
    }

    @Transactional
    public void logout2(String authorizationHeader) {
        String token = authorizationHeader.substring(7);
        jwtTokenService.blacklistToken(token);
        SecurityContextHolder.clearContext();
    }

    public UserResponse getUserDashboard() {
        User user = getCurrentUserInfo();
        return new UserResponse(
                user.getName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getAddress(),
                user.getAccount().getAccountNumber().toString().substring(0, 6),
                user.getHashedPassword()
        );
    }

    public AccountResponse getAccountDashboard() {
        Account account = getCurrentAccountInfo();
        return new AccountResponse(account.getAccountNumber().toString().substring(0, 6), account.getBalance());
    }
}
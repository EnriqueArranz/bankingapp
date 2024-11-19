package com.hackathon.bankingapp.Controllers;

import com.hackathon.bankingapp.DTO.LoginRequest;
import com.hackathon.bankingapp.DTO.UserRegistrationRequest;
import com.hackathon.bankingapp.Security.JwtTokenService;
import com.hackathon.bankingapp.Services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @Autowired
    private JwtTokenService jwtTokenService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRegistrationRequest request) {
        return ResponseEntity.ok(userService.registerUser(request));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(userService.login(request));
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String authorizationHeader) {
        userService.logout2(authorizationHeader);
        return ResponseEntity.ok("Logged out successfully");
    }


}

package com.hackathon.bankingapp.Controllers;

import com.hackathon.bankingapp.Services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth/password-reset")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/send-otp")
    public ResponseEntity<?> sendOtp(@RequestBody Map<String, String> request) {
        String identifier = request.get("identifier");
        if (authService.sendOtp(identifier)) {
            return ResponseEntity.ok(Map.of("message", "OTP sent successfully to: " + identifier));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error sending OTP.");
        }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody Map<String, String> request) {
        String identifier = request.get("identifier");
        String otp = request.get("otp");
        String resetToken = authService.verifyOtp(identifier, otp);

        if (resetToken != null) {
            return ResponseEntity.ok(Map.of("passwordResetToken", resetToken));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid OTP");
        }
    }

    @PostMapping
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {
        String identifier = request.get("identifier");
        String resetToken = request.get("resetToken");
        String newPassword = request.get("newPassword");

        if (authService.resetPassword(identifier, resetToken, newPassword)) {
            return ResponseEntity.ok(Map.of("message", "Password reset successfully"));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid reset token or other error.");
        }
    }
}
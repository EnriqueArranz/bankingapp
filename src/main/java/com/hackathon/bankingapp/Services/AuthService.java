package com.hackathon.bankingapp.Services;

import com.hackathon.bankingapp.Entities.Otp;
import com.hackathon.bankingapp.Entities.User;
import com.hackathon.bankingapp.Repositories.OtpRepository;
import com.hackathon.bankingapp.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    @Autowired
    private  UserRepository userRepository;
    @Autowired
    private  OtpRepository otpRepository;
    @Autowired
    private  JavaMailSender mailSender;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean sendOtp(String identifier) {
        Optional<User> user = userRepository.findByEmail(identifier);
        if (user != null) {
            String otp = generateOtp();
            otpRepository.save(new Otp(identifier, otp, LocalDateTime.now()));
            sendOtpEmail(identifier, otp);
            return true;
        }
        return false;
    }

    public String verifyOtp(String identifier, String otp) {
        Optional<Otp> otpEntry = otpRepository.findByIdentifierAndOtp(identifier, otp);
        if (otpEntry.isPresent() && isOtpValid(otpEntry.get())) {
            String resetToken = UUID.randomUUID().toString();
            otpRepository.saveResetToken(identifier, resetToken);
            return resetToken;
        }
        return null;
    }

    public boolean resetPassword(String identifier, String resetToken, String newPassword) {
        Optional<User> user = userRepository.findByEmail(identifier);
        if (user.isPresent() && otpRepository.verifyResetToken(identifier, resetToken)) {
            user.get().setHashedPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user.get());
            otpRepository.invalidateResetToken(identifier);
            return true;
        }
        return false;
    }

    private String generateOtp() {
        return String.format("%06d", new Random().nextInt(999999));
    }

    private boolean isOtpValid(Otp otp) {
        return otp.getTimestamp().isAfter(LocalDateTime.now().minusMinutes(10));
    }

    private void sendOtpEmail(String to, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Your OTP Code");
        message.setText("OTP:" + otp);
        mailSender.send(message);
    }
}
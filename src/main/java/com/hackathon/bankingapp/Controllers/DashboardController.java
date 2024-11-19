package com.hackathon.bankingapp.Controllers;

import com.hackathon.bankingapp.Services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DashboardController {


@Autowired
private UserService userService;

    @GetMapping("/dashboard/user")
    public ResponseEntity<?> getUserDashboard() {
        return ResponseEntity.ok(userService.getUserDashboard());
    }

    @GetMapping("/dashboard/account")
    public ResponseEntity<?> getAccountDashboard() {
        return ResponseEntity.ok(userService.getAccountDashboard());
    }
}

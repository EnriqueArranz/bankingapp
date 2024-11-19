package com.hackathon.bankingapp.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserResponse {

    private String name;
    private String email;
    private String phoneNumber;
    private String address;
    private String accountNumber;
    private String hashedPassword;

}
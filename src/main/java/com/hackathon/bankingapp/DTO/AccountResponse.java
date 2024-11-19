package com.hackathon.bankingapp.DTO;

import com.hackathon.bankingapp.Entities.Account;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AccountResponse {
    private String accountNumber;
    private double balance;

}
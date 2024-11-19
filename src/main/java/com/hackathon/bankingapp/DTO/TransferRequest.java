package com.hackathon.bankingapp.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class TransferRequest {
    private double amount;
    private String pin;
    private String targetAccountNumber;

}

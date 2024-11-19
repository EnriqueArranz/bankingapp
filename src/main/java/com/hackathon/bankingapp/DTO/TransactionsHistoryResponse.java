package com.hackathon.bankingapp.DTO;

import com.hackathon.bankingapp.Entities.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class TransactionsHistoryResponse {

    private UUID id;
    private double amount;
    private TransactionType transactionType;
    private long transactionDate; // Epoch timestamp
    private String sourceAccountNumber;
    private String targetAccountNumber;

}

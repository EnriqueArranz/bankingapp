package com.hackathon.bankingapp.Services;

import com.hackathon.bankingapp.DTO.*;
import com.hackathon.bankingapp.Entities.Account;
import com.hackathon.bankingapp.Entities.Transaction;
import com.hackathon.bankingapp.Entities.TransactionType;
import com.hackathon.bankingapp.Entities.User;
import com.hackathon.bankingapp.Exceptions.CustomException;
import com.hackathon.bankingapp.Repositories.AccountRepository;
import com.hackathon.bankingapp.Repositories.TransactionRepository;
import com.hackathon.bankingapp.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;

    public DepositResponse deposit(DepositRequest request) {
        Account account = getCurrentUserAccount();
        double amount = request.getAmount();
        String pin = request.getPin();
        if (account.getPin() == null) {
            throw new CustomException("PIN not set", HttpStatus.BAD_REQUEST);
        }
        validatePin(pin, account);
        account.setBalance(account.getBalance() + amount);
        accountRepository.save(account);
        recordTransaction(account.getAccountNumber().toString(), amount, TransactionType.CASH_DEPOSIT, null);
        return new DepositResponse("Cash deposited successfully");
    }

    public WithdrawResponse withdraw(WithdrawRequest request) {
        Account account = getCurrentUserAccount();
        double requestedAmount = request.getAmount();
        String pin = request.getPin();
        if (account.getPin() == null) {
            throw new CustomException("PIN not set", HttpStatus.BAD_REQUEST);
        }
        if (requestedAmount <= 0) {
            throw new CustomException("Invalid amount", HttpStatus.BAD_REQUEST);
        }
        validatePin(pin, account);
        if (account.getBalance() < requestedAmount) {
            throw new CustomException("Insufficient balance", HttpStatus.BAD_REQUEST);
        }
        account.setBalance(account.getBalance() - requestedAmount);
        accountRepository.save(account);
        recordTransaction(account.getAccountNumber()
                .toString(), requestedAmount, TransactionType.CASH_WITHDRAWAL, null);
        return new WithdrawResponse("Cash withdrawn successfully");
    }

    public TransferResponse transfer(TransferRequest request) {
        Account account = getCurrentUserAccount();
        String targetAccountNumber = request.getTargetAccountNumber();
        double amount = request.getAmount();
        String pin = request.getPin();
        validatePin(pin, account);
        if (!accountRepository.existsByAccountNumber(targetAccountNumber)) {
            throw new CustomException("Target account not found", HttpStatus.BAD_REQUEST);
        }

        if (account.getPin() == null) {
            throw new CustomException("PIN not set", HttpStatus.BAD_REQUEST);
        }
        if (request.getPin() == null) {
            throw new CustomException("PIN not provided", HttpStatus.BAD_REQUEST);
        }
        if (request.getTargetAccountNumber() == null) {
            throw new CustomException("Target account number not provided", HttpStatus.BAD_REQUEST);
        }

        if ((account).getAccountNumber().toString().equals(targetAccountNumber)) {
            throw new CustomException("Cannot transfer to same account", HttpStatus.BAD_REQUEST);
        }
        if (request.getAmount() <= 0) {
            throw new CustomException("Invalid amount", HttpStatus.BAD_REQUEST);
        }
        if ((account).getBalance() < amount) {
            throw new CustomException("Insufficient balance", HttpStatus.BAD_REQUEST);
        }
        Account targetAccount = accountRepository.findByAccountNumber(targetAccountNumber);
        (account).setBalance((account).getBalance() - amount);
        targetAccount.setBalance(targetAccount.getBalance() + amount);
        accountRepository.save(account);
        accountRepository.save(targetAccount);
        recordTransaction((account).getAccountNumber()
                .toString(), amount, TransactionType.CASH_TRANSFER, targetAccountNumber.toString());
        return new TransferResponse("Fund transferred successfully");
    }

    private void recordTransaction(String sourceAccountNumber, double amount, TransactionType transactionType, String targetAccountNumber) {
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setTransactionType(transactionType);
        transaction.setTransactionDateTime(LocalDateTime.now());
        transaction.setSourceAccountNumber(sourceAccountNumber);
        if (targetAccountNumber != null) {
            transaction.setTargetAccountNumber(targetAccountNumber);
        }
        transaction.setAssetSymbol("N/A");
        transaction.setUser(getCurrentUser());
        transactionRepository.save(transaction);
    }

    public List<TransactionsHistoryResponse> getTransactionHistory() {
        List<Transaction> transactions = transactionRepository.findAll();
        return transactions.stream()
                .map(transaction -> new TransactionsHistoryResponse(
                        transaction.getId(),
                        transaction.getAmount(),
                        transaction.getTransactionType(),
                        transaction.getTransactionDateTime().toInstant(ZoneOffset.UTC).toEpochMilli(),
                        transaction.getSourceAccountNumber(),
                        transaction.getTargetAccountNumber() != null ? transaction.getTargetAccountNumber() : "N/A"
                ))
                .collect(Collectors.toList());
    }

    private void validatePin(String pin, Account account) {
        if (!passwordEncoder.matches(pin, account.getPin().getHashedPin())) {
            throw new CustomException("Invalid PIN", HttpStatus.BAD_REQUEST);
        }
    }

    public List<Transaction> getTransactionHistory(Account account) {
        return transactionRepository.findBySourceAccountNumber(account.getAccountNumber().toString());
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // Method to get current user's account
    private Account getCurrentUserAccount() {
        User user = getCurrentUser();
        return user.getAccount();
    }
}
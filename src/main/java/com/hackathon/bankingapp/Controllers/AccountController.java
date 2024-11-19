package com.hackathon.bankingapp.Controllers;

import com.hackathon.bankingapp.DTO.*;
import com.hackathon.bankingapp.Services.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class AccountController {
    @Autowired
    private final AccountService accountService;
    @Autowired
    private final PinService pinService;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private MarketService marketService;
    @Autowired
    private AssetService assetService;

    @PostMapping("/pin/create")
    public ResponseEntity<?> createPin(@RequestBody PinCreateRequest request) {
        PinCreateResponse response = pinService.createPin(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/pin/update")
    public ResponseEntity<?> updatePin(@RequestBody PinUpdateRequest request) {
        PinUpdateResponse response = pinService.updatePin(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/deposit")
    public ResponseEntity<?> deposit(@RequestBody DepositRequest request) {
        DepositResponse response = transactionService.deposit(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<?> withdraw(@RequestBody WithdrawRequest request) {
        WithdrawResponse response = transactionService.withdraw(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/fund-transfer")
    public ResponseEntity<?> transfer(@RequestBody TransferRequest request) {
        TransferResponse response = transactionService.transfer(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/transactions")
    public ResponseEntity<?> getTransactionHistory() {
        List<TransactionsHistoryResponse> response = transactionService.getTransactionHistory();
        return ResponseEntity.ok(response);
    }

//    @PostMapping("/buy-asset")
//    public ResponseEntity<?> getAssets(@RequestBody BuyAssetRequest request) {
//        List<TransactionsHistoryResponse> response = transactionService.getTransactionHistory();
//        return ResponseEntity.ok(response);
//    }

}
package com.hackathon.bankingapp.Services;

import com.hackathon.bankingapp.DTO.MessageResponseDto;
import com.hackathon.bankingapp.DTO.BuyAssetRequest;
import com.hackathon.bankingapp.Entities.Account;
import com.hackathon.bankingapp.Entities.User;
import com.hackathon.bankingapp.Exceptions.CustomException;
import com.hackathon.bankingapp.Repositories.AccountRepository;
import com.hackathon.bankingapp.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MarketService {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private MarketApiClient marketApiClient;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JavaMailSender mailSender;



//    public MessageResponseDto buyAsset(BuyAssetRequest request) {
//        Account account = getCurrentUserAccount();
//        User user = getCurrentUser();
//
//
//        Map<String, Double> assetMap = marketApiClient.getAssetPrice(request.getAssetSymbol());
//        if (assetMap == null) {
//            throw new CustomException("Internal error occurred while purchasing the asset.", HttpStatus.BAD_REQUEST);
//        }
//        double purchasePrice = marketApiClient.getAssetPrice(request.getAssetSymbol()).get(request.getAssetSymbol());
//
//        double units = request.getAmount() / purchasePrice;
//        if (account.getBalance() < request.getAmount()) {
//            throw new CustomException("Internal error occurred while purchasing the asset.", HttpStatus.BAD_REQUEST);
//        }
//
//        account.setBalance(account.getBalance() - request.getAmount());
//        Asset newAsset = new Asset();
//        newAsset.setSymbol(request.getAssetSymbol());
//        newAsset.setPurchasePrice(purchasePrice);
//        newAsset.setUnits(units);
//        newAsset.setUser(user);
//        assetRepository.save(newAsset);
//        userRepository.save(user);
//        return new MessageResponseDto("Asset purchase successful.");
//
//    }
//    public MessageResponseDto buyAsset2(BuyAssetRequest request) {
//        Account account = getCurrentUserAccount();
//        User user = getCurrentUser();
//
//
//        Map<String, Double> assetMap = marketApiClient.getAssetPrice(request.getAssetSymbol());
//        if (assetMap == null) {
//            throw new CustomException("Internal error occurred while purchasing the asset.", HttpStatus.BAD_REQUEST);
//        }
//        double purchasePrice = marketApiClient.getAssetPrice(request.getAssetSymbol()).get(request.getAssetSymbol());
//
//        double units = request.getAmount() / purchasePrice;
//        if (account.getBalance() < request.getAmount()) {
//            throw new CustomException("Internal error occurred while purchasing the asset.", HttpStatus.BAD_REQUEST);
//        }
//
//        account.setBalance(account.getBalance() - request.getAmount());
//        Asset newAsset = new Asset();
//        newAsset.setSymbol(request.getAssetSymbol());
//        newAsset.setPurchasePrice(purchasePrice);
//        newAsset.setUnits(units);
//        newAsset.setUser(user);
//        assetRepository.save(newAsset);
//        userRepository.save(user);
//        return new MessageResponseDto("Asset purchase successful.");
//
//    }
//    private void sendPurchaseEmail(String to, String otp) {
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(to);
//        message.setSubject("Investment Purchase Confirmation");
//        message.setText("OTP:" + otp);
//        mailSender.send(message);
//    }
//
//    private User getCurrentUser() {
//        String email = SecurityContextHolder.getContext().getAuthentication().getName();
//        return userRepository.findByEmail(email)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//    }
//
//
//    private Account getCurrentUserAccount() {
//        User user = getCurrentUser();
//        return user.getAccount();
//    }
//
//    public String sellAsset(SellAssetRequestDto request, String jwt) {
//        // Authenticate PIN and JWT
//        Account account = authenticateAndGetAccount(request.getPin(), jwt);
//
//        // Fetch asset and get real-time price
//        Asset asset = assetRepository.findBySymbolAndAccount(request.getAssetSymbol(), account);
//        if (asset == null || asset.getUnits() < request.getQuantity()) {
//            throw new InsufficientAssetsException("Not enough assets to sell");
//        }
//
//        double realTimePrice = marketApiClient.getAssetPrice(request.getAssetSymbol());
//        double gainOrLoss = (realTimePrice - asset.getPurchasePrice()) * request.getQuantity();
//
//        // Update account balance and asset quantity
//        account.setBalance(account.getBalance() + (request.getQuantity() * realTimePrice));
//        asset.setUnits(asset.getUnits() - request.getQuantity());
//        assetRepository.save(asset);
//
//        // Send email notification
//        emailService.sendInvestmentConfirmation(account, asset, "sale", gainOrLoss);
//
//        return "Asset sale successful.";
//    }
//
//    public Double calculateNetWorth(String jwt) {
//        Account account = getCurrentUserAccount();
//        double netWorth = account.getBalance();
//        for (Asset asset : account.getAssets()) {
//            double realTimePrice = marketApiClient.getAssetPrice(asset.getSymbol());
//            netWorth += asset.getUnits() * realTimePrice;
//        }
//        return netWorth;
//    }
//
//    public RealTimePricesResponseDto getAllMarketPrices() {
//
//    }

//    public MessageResponseDto getAssetPrice(String symbol) {
//        return marketApiClient.getAssetPrice(symbol);
//    }
//    public RealTimePricesResponseDto getAllMarketPrices() {
//        return marketApiClient.getAllMarketPricesShow();
//    }
//    public Double getAssetPriceDouble(String symbol) {
//        return marketApiClient.getAssetPriceDouble(symbol);
//    }

}
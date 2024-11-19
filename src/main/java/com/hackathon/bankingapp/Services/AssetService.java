package com.hackathon.bankingapp.Services;

import com.hackathon.bankingapp.DTO.AssetsListResponse;
import com.hackathon.bankingapp.Entities.Account;
import com.hackathon.bankingapp.Entities.User;
import com.hackathon.bankingapp.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AssetService {


    @Autowired
    private MarketService marketService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MarketApiClient marketApiClient;

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }


    private Account getCurrentUserAccount() {
        User user = getCurrentUser();
        return user.getAccount();
    }

//    public List<AssetsListResponse> getAggregatedAssetsForUser() {
//        // Fetch user and their assets
//        Account account = getCurrentUserAccount();
//        User user = account.getUser();
//        Asset asset = new Asset();
//
//        // Aggregate assets by symbol and sum units
//        Map<String, Double> aggregatedAssets = user.getAssets().stream()
//                .collect(Collectors.groupingBy(
//                        Asset::getSymbol,
//                        Collectors.summingDouble(Asset::getUnits)
//                ));
//
//        // Get market prices
//        Map<String, Double> marketPrices = marketApiClient.getAllMarketPrices();
//
//        // Calculate total value and create response DTOs
//        return aggregatedAssets.entrySet().stream()
//                .map(entry -> {
//                    String symbol = entry.getKey();
//                    Double totalUnits = entry.getValue();
//                    Double currentPrice = marketPrices.get(symbol);
//
//                    // Calculate total value if current price is available
//                    Double totalValue = (currentPrice != null) ? totalUnits * currentPrice : 0.0;
//                    return new AssetsListResponse(symbol, totalValue);
//                })
//                .collect(Collectors.toList());
//    }
}

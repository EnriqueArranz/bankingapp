package com.hackathon.bankingapp.Controllers;

import com.hackathon.bankingapp.Services.MarketApiClient;
import com.hackathon.bankingapp.Services.MarketService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/market")
@RequiredArgsConstructor
public class MarketController {
    @Autowired
    private MarketService marketService;
    @Autowired
    private MarketApiClient marketApiClient;

    @GetMapping("/prices")
    public ResponseEntity<Map<String, Double>> getMarketPrices() {
        Map<String, Double> prices = marketApiClient.getAllMarketPrices();
        return ResponseEntity.ok(prices);
    }

    @GetMapping("/prices/{symbol}")
    public ResponseEntity<?> getAssetPrice(@PathVariable String symbol) {
        Map<String, Double> price = marketApiClient.getAssetPrice(symbol);
        return ResponseEntity.ok(price);
    }
}
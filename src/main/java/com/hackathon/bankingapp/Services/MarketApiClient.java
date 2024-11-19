package com.hackathon.bankingapp.Services;

import com.hackathon.bankingapp.DTO.RealTimePricesResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class MarketApiClient {
    private final String MARKET_API_URL = "https://faas-lon1-917a94a7.doserverless.co/api/v1/web/fn-e0f31110-7521-4cb9-86a2-645f66eefb63/default/market-prices-simulator";
    @Autowired
    private RestTemplate restTemplate;

    public Map<String, Double> getAllMarketPrices() {
        try {
            return restTemplate.getForObject(MARKET_API_URL, Map.class);
        } catch (RestClientException e) {
            throw new RuntimeException("Error fetching market prices: " + e.getMessage(), e);
        }
    }


    public Map<String, Double> getAssetPrice(String symbol) {
        Map<String, Double> marketPrices = getAllMarketPrices();
        if (marketPrices.containsKey(symbol)) {
            return Map.of(symbol, marketPrices.get(symbol));
        } else {
            throw new RuntimeException("Symbol not found: " + symbol);
        }
    }
    public RealTimePricesResponseDto getAllMarketPricesShow() {
        try {
            return restTemplate.getForObject(MARKET_API_URL, RealTimePricesResponseDto.class);
        } catch (RestClientException e) {
            throw new RuntimeException("Error fetching market prices: " + e.getMessage(), e);
        }
    }

    public Double getAssetPriceDouble(String symbol) {
        Map<String, Double> assetPrices = getAllMarketPricesShow().getAssetPrices();
        if (assetPrices.containsKey(symbol)) {
            return assetPrices.get(symbol);
        } else {
            throw new RuntimeException("Symbol not found: " + symbol);
        }
    }

}
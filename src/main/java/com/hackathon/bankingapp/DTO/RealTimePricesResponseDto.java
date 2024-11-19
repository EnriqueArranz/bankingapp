package com.hackathon.bankingapp.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RealTimePricesResponseDto {
    private Map<String, Double> assetPrices;
}

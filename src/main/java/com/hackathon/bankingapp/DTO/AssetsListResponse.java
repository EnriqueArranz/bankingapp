package com.hackathon.bankingapp.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AssetsListResponse {
    private String symbol;
    private Double totalValue;
}

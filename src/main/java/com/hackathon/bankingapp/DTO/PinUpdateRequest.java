package com.hackathon.bankingapp.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PinUpdateRequest {
    private String oldPin;
    private String password;
    private String newPin;
}

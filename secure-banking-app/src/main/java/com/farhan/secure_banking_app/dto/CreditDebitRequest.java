package com.farhan.secure_banking_app.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreditDebitRequest {
    private String accountNumber;
    private BigDecimal amount;
}

package com.farhan.secure_banking_app.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDTO {
    private String transactionType;
    private BigDecimal transactionAmount;
    private String transactionStatus;
    private String accountNumber;
}

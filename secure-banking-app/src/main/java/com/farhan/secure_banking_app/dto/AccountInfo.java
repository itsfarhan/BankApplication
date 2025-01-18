package com.farhan.secure_banking_app.dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountInfo {

    @Schema(name = "Account Number")
    private String accountNumber;

    @Schema(name = "Account Balance")
    private BigDecimal accountBalance;

    @Schema(name = "Account Name")
    private String accountName;
}

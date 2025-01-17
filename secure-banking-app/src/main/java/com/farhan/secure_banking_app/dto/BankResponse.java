package com.farhan.secure_banking_app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class BankResponse {
    private String responsecode;
    private String responseMessage;
    private AccountInfo accountInfo;
}

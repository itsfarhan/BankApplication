package com.farhan.secure_banking_app.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.farhan.secure_banking_app.services.BankStatement;
import com.farhan.secure_banking_app.entity.Transaction;
 
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/bankStatement")
@AllArgsConstructor
public class TransactionController {
    private BankStatement bankStatement;

    @GetMapping("/transactions")
    public List<Transaction> getTransactions(
    @RequestParam String accountNumber,
    @RequestParam String startDate,
    @RequestParam String endDate) {
        return bankStatement.getTransactions(accountNumber, startDate, endDate);
    }
}

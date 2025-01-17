package com.farhan.secure_banking_app.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.farhan.secure_banking_app.dto.TransactionDTO;
import com.farhan.secure_banking_app.entity.Transaction;
import com.farhan.secure_banking_app.repo.TransactionRepo;

@Component
public class TransactionServiceImpl implements TransactionService {
    
    @Autowired
    TransactionRepo transactionRepo;
    
    @Override
    public void saveTransaction(TransactionDTO transactionDTO) {
        Transaction transaction = Transaction.builder()
                .transactionType(transactionDTO.getTransactionType())
                .transactionAmount(transactionDTO.getTransactionAmount())
                .accountNumber(transactionDTO.getAccountNumber())
                .transactionStatus("SUCCESS")
                .build();
        transactionRepo.save(transaction);
    }
    
}

package com.farhan.secure_banking_app.services;


import org.springframework.stereotype.Service;

import com.farhan.secure_banking_app.dto.TransactionDTO;

@Service
public interface TransactionService {
    void saveTransaction(TransactionDTO transactionDTO);
} 
  


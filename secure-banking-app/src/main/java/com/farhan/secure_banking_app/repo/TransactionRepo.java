package com.farhan.secure_banking_app.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.farhan.secure_banking_app.entity.Transaction;

public interface TransactionRepo extends JpaRepository<Transaction, String> {
    
}

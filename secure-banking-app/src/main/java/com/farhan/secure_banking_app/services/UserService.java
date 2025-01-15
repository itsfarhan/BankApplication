package com.farhan.secure_banking_app.services;

import org.springframework.boot.autoconfigure.security.SecurityProperties.User;

import com.farhan.secure_banking_app.dto.BankResponse;
import com.farhan.secure_banking_app.dto.UserRequest;

public interface UserService {
    BankResponse createAccount(UserRequest userRequest);    
    
}

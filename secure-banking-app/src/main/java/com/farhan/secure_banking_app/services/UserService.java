package com.farhan.secure_banking_app.services;


import com.farhan.secure_banking_app.dto.BankResponse;
import com.farhan.secure_banking_app.dto.UserRequest;

public interface UserService {
    BankResponse createAccount(UserRequest userRequest);    
    
}

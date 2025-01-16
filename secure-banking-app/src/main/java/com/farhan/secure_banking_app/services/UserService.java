package com.farhan.secure_banking_app.services;


import com.farhan.secure_banking_app.dto.BankResponse;
import com.farhan.secure_banking_app.dto.CreditDebitRequest;
import com.farhan.secure_banking_app.dto.EnquiryRequest;
import com.farhan.secure_banking_app.dto.UserRequest;

public interface UserService {
    BankResponse createAccount(UserRequest userRequest);    
    BankResponse balanceEnquiry(EnquiryRequest enquiryRequest);
    String nameEnquire(EnquiryRequest enquiryRequest);
    BankResponse creditAccount(CreditDebitRequest creditDebitRequest); 
    BankResponse debitAccount(CreditDebitRequest creditDebitRequest);
}

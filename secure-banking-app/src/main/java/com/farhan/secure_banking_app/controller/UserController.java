package com.farhan.secure_banking_app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.farhan.secure_banking_app.dto.BankResponse;
import com.farhan.secure_banking_app.dto.CreditDebitRequest;
import com.farhan.secure_banking_app.dto.EnquiryRequest;
import com.farhan.secure_banking_app.dto.UserRequest;
import com.farhan.secure_banking_app.services.UserService;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/user")
public class UserController {
    
    @Autowired
    UserService userService;

    @PostMapping
    public BankResponse createAccount(@RequestBody UserRequest userRequest) {
        return userService.createAccount(userRequest);
    }

    @GetMapping("/balanceEnquiry")
    public BankResponse balanceEnquiry (@RequestBody EnquiryRequest enquiryRequest){
        return userService.balanceEnquiry(enquiryRequest);
    }

    @GetMapping("/nameEnquiry")
        public String nameEnquiry(@RequestBody EnquiryRequest enquiryRequest){
            return userService.nameEnquire(enquiryRequest);
    }

    @PostMapping("/creditAccount")
    public BankResponse creditAccount(@RequestBody CreditDebitRequest creditDebitRequest){
        return userService.creditAccount(creditDebitRequest);
    }
    
    @PostMapping("/debitAccount")
    public BankResponse debitAccount(@RequestBody CreditDebitRequest creditDebitRequest){
        return userService.debitAccount(creditDebitRequest);
    }
}
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
import com.farhan.secure_banking_app.dto.TransferRequest;
import com.farhan.secure_banking_app.dto.UserRequest;
import com.farhan.secure_banking_app.services.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("/api/user")
@Tag(name = "User API", description = "User Account Management API") 
public class UserController {
    
    @Autowired
    UserService userService;

    @Operation(summary = "Create Account", description = "This API is used to create a new account")
    @ApiResponse(responseCode = "200", description = "Account created successfully")
    @PostMapping
    public BankResponse createAccount(@RequestBody UserRequest userRequest) {
        return userService.createAccount(userRequest);
    }

    @Operation(summary = "Balance Enquiry", description = "This API is used to check the balance of an account")
    @ApiResponse(responseCode = "200", description = "Balance Enquiry successful")
    @GetMapping("/balanceEnquiry")
    public BankResponse balanceEnquiry (@RequestBody EnquiryRequest enquiryRequest){
        return userService.balanceEnquiry(enquiryRequest);
    }

    @Operation(summary = "Name Enquiry", description = "This API is used to check the name of an account holder")
    @ApiResponse(responseCode = "200", description = "Name Enquiry successful")
    @GetMapping("/nameEnquiry")
        public String nameEnquiry(@RequestBody EnquiryRequest enquiryRequest){
            return userService.nameEnquire(enquiryRequest);
    }

    @Operation(summary = "Account Statement", description = "This API is used to get the account statement")
    @ApiResponse(responseCode = "200", description = "Account Statement fetched successfully")
    @PostMapping("/creditAccount")
    public BankResponse creditAccount(@RequestBody CreditDebitRequest creditDebitRequest){
        return userService.creditAccount(creditDebitRequest);
    }

    @Operation(summary = "Debit Account", description = "This API is used to debit the account")
    @ApiResponse(responseCode = "200", description = "Account debited successfully")
    @PostMapping("/debitAccount")
    public BankResponse debitAccount(@RequestBody CreditDebitRequest creditDebitRequest){
        return userService.debitAccount(creditDebitRequest);
    }

    @Operation(summary = "Transfer Amount", description = "This API is used to transfer amount from one account to another")
    @ApiResponse(responseCode = "200", description = "Amount transferred successfully")
    @PostMapping("/transferAmount")
    public BankResponse transferAmount(@RequestBody TransferRequest transferRequest){
        return userService.transferAmount(transferRequest);
    }
}
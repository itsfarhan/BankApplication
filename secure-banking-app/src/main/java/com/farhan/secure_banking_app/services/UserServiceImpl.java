package com.farhan.secure_banking_app.services;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.farhan.secure_banking_app.entity.User;
import com.farhan.secure_banking_app.repo.UserRepository;
import com.farhan.secure_banking_app.dto.AccountInfo;
import com.farhan.secure_banking_app.dto.BankResponse;
import com.farhan.secure_banking_app.dto.EmailDetails;
import com.farhan.secure_banking_app.dto.UserRequest;
import com.farhan.secure_banking_app.utils.AccountUtils;



@Service
public class UserServiceImpl implements UserService {
        
        @Autowired
        UserRepository userRepository;

        @Autowired
        EmailService emailService;

        @Override
        public BankResponse createAccount(UserRequest userRequest) {
            /*
            if(userRepository.existsByEmail(userRequest.getEmail())) {
             * Check if the user already has an account
             */
            if(userRepository.existsByEmail(userRequest.getEmail())) {
                return BankResponse.builder()
                        .responsecode(AccountUtils.ACCOUNT_EXIST_CODE)
                        .responseMessage(AccountUtils.ACCOUNT_EXIST_MESSAGE)
                        .accountInfo(null)
                        .build();
            }

                User newUser = User.builder()
                        .firstName(userRequest.getFirstName())
                        .lastName(userRequest.getLastName())
                        .middleName(userRequest.getMiddleName())
                        .gender(userRequest.getGender())
                        .address(userRequest.getAddress())
                        .country(userRequest.getCountry())
                        .accountNumber(AccountUtils.generateAccountNumber())
                        .accountBalance(BigDecimal.ZERO)
                        .email(userRequest.getEmail())
                        .phoneNumber(userRequest.getPhoneNumber())
                        .alternativePhoneNumber(userRequest.getAlternativePhoneNumber())
                        .status("ACTIVE")
                        .build(); 

                        User savedUser = userRepository.save(newUser);
                        //Send email alert
                        EmailDetails emailDetails = EmailDetails.builder()
                                .recipient(savedUser.getEmail())
                                .subject("ACCOUNT_CREATION")
                                .messageBody("Congratulations " + savedUser.getFirstName() + " " + savedUser.getLastName() + " " + " your account has been created successfully. Your account number is " + savedUser.getAccountNumber())
                                .build();
                        emailService.sendEmailAlert(emailDetails);

                        return BankResponse.builder()
                        .responsecode(AccountUtils.ACCOUNT_CREATION_SUCCESS)
                        .responseMessage(AccountUtils.ACCOUNT_CREATION_MESSAGE)
                        .accountInfo(AccountInfo.builder()
                                .accountBalance(savedUser.getAccountBalance())
                                .accountNumber(savedUser.getAccountNumber())
                                .accountName(savedUser.getFirstName() + " " + savedUser.getLastName() + " " + savedUser.getMiddleName()) 
                                .build())
                        .build();
        }
}

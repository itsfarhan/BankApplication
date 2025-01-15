package com.farhan.secure_banking_app.services;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.farhan.secure_banking_app.entity.User;
import com.farhan.secure_banking_app.repo.UserRepository;
import com.farhan.secure_banking_app.dto.BankResponse;
import com.farhan.secure_banking_app.dto.UserRequest;
import com.farhan.secure_banking_app.utils.AccountUtils;

import lombok.Builder;

@Service
public class UserServiceImpl implements UserService {
        
        @Autowired
        private UserRepository userRepository;

        @Override
        public BankResponse createAccount(UserRequest userRequest) {
            /*
            if(userRepository.existsByEmail(userRequest.getEmail())) {
             * Check if the user already has an account
             */
            if(userRepository.existsByEmail(userRequest.getEmail())) {
                BankResponse response = BankResponse.builder()
                        .responsecode(AccountUtils.ACCOUNT_EXIST_CODE)
                        .responseMessage(AccountUtils.ACCOUNT_EXIST_MESSAGE)
                        .accountInfo(null)
                        .build();
                return response;
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
        }
}

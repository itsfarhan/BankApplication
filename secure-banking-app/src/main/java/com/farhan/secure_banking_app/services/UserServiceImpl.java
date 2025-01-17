package com.farhan.secure_banking_app.services;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.farhan.secure_banking_app.entity.User;
import com.farhan.secure_banking_app.repo.UserRepository;
import com.farhan.secure_banking_app.dto.AccountInfo;
import com.farhan.secure_banking_app.dto.BankResponse;
import com.farhan.secure_banking_app.dto.CreditDebitRequest;
import com.farhan.secure_banking_app.dto.EmailDetails;
import com.farhan.secure_banking_app.dto.EnquiryRequest;
import com.farhan.secure_banking_app.dto.TransactionDTO;
import com.farhan.secure_banking_app.dto.TransferRequest;
import com.farhan.secure_banking_app.dto.UserRequest;
import com.farhan.secure_banking_app.utils.AccountUtils;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

        @Autowired
        UserRepository userRepository;
        EmailService emailService;
        TransactionService transactionService;
        @Autowired
        PasswordEncoder passwordEncoder;

        @Override
        public BankResponse createAccount(UserRequest userRequest) {
                /*
                 * if(userRepository.existsByEmail(userRequest.getEmail())) {
                 * Check if the user already has an account
                 */
                if (userRepository.existsByEmail(userRequest.getEmail())) {
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
                                .password(passwordEncoder.encode(userRequest.getPassword()))
                                .phoneNumber(userRequest.getPhoneNumber())
                                .alternativePhoneNumber(userRequest.getAlternativePhoneNumber())
                                .status("ACTIVE")
                                .build();
                userRepository.save(newUser);
                // Send email alert
                EmailDetails emailDetails = EmailDetails.builder()
                                .recipient(newUser.getEmail())
                                .subject("ACCOUNT_CREATION")
                                .messageBody("Your account has been created successfully with account number "
                                                + newUser.getAccountNumber() + " on " + java.time.LocalDateTime.now())
                                .build();
                emailService.sendEmailAlert(emailDetails);

                return BankResponse.builder()
                                .responsecode(AccountUtils.ACCOUNT_CREATION_SUCCESS)
                                .responseMessage(AccountUtils.ACCOUNT_CREATION_MESSAGE)
                                .accountInfo(AccountInfo.builder()
                                                .accountBalance(newUser.getAccountBalance())
                                                .accountName(newUser.getFirstName() + " " + newUser.getLastName() + " "
                                                                + newUser.getMiddleName())
                                                .accountNumber(newUser.getAccountNumber())
                                                .build())
                                .build();
        }

        @Override
        public BankResponse balanceEnquiry(EnquiryRequest enquiryRequest) {
                boolean isAccountExist = userRepository.existsByAccountNumber(enquiryRequest.getAccountNumber());
                if (!isAccountExist) {
                        return BankResponse.builder()
                                        .responsecode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
                                        .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                                        .accountInfo(null)
                                        .build();
                }
                User foundUser = userRepository.findByAccountNumber(enquiryRequest.getAccountNumber());
                return BankResponse.builder()
                                .responseMessage(AccountUtils.ACCOUNT_FOUND_MESSAGE)
                                .responsecode(AccountUtils.ACCOUNT_FOUND_CODE)
                                .accountInfo(AccountInfo.builder()
                                                .accountBalance(foundUser.getAccountBalance())
                                                .accountName(foundUser.getFirstName() + " " + foundUser.getLastName()
                                                                + " " + foundUser.getMiddleName())
                                                .accountNumber(foundUser.getAccountNumber())
                                                .build())
                                .build();
        }

        @Override
        public String nameEnquire(EnquiryRequest enquiryRequest) {
                boolean isAccountExist = userRepository.existsByAccountNumber(enquiryRequest.getAccountNumber());
                if (!isAccountExist) {
                        return AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE;
                }
                User foundUser = userRepository.findByAccountNumber(enquiryRequest.getAccountNumber());
                return foundUser.getFirstName() + " " + foundUser.getLastName() + " " + foundUser.getMiddleName();
        }

        @Override
        public BankResponse creditAccount(CreditDebitRequest creditDebitRequest) {
                boolean isAccountExist = userRepository.existsByAccountNumber(creditDebitRequest.getAccountNumber());
                if (!isAccountExist) {
                        return BankResponse.builder()
                                        .responsecode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
                                        .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                                        .accountInfo(null)
                                        .build();
                }
                User userToCredit = userRepository.findByAccountNumber(creditDebitRequest.getAccountNumber());
                userToCredit.setAccountBalance(userToCredit.getAccountBalance().add(creditDebitRequest.getAmount()));
                userRepository.save(userToCredit);

                TransactionDTO transactionDTO = TransactionDTO.builder()
                                .transactionType("CREDIT")
                                .transactionAmount(creditDebitRequest.getAmount())
                                .accountNumber(userToCredit.getAccountNumber())
                                .transactionStatus("SUCCESS")
                                .build();

                transactionService.saveTransaction(transactionDTO);

                return BankResponse.builder()
                                .responsecode(AccountUtils.ACCOUNT_CREDITED_SUCCESS)
                                .responseMessage(AccountUtils.ACCOUNT_CREDITED_SUCCESS_MESSAGE)
                                .accountInfo(AccountInfo.builder()
                                                .accountBalance(userToCredit.getAccountBalance())
                                                .accountName(userToCredit.getFirstName() + " "
                                                                + userToCredit.getLastName() + " "
                                                                + userToCredit.getMiddleName())
                                                .accountNumber(userToCredit.getAccountNumber())
                                                .build())
                                .build();
        }

        @Override
        public BankResponse debitAccount(CreditDebitRequest creditDebitRequest) {
                boolean isAccountExist = userRepository.existsByAccountNumber(creditDebitRequest.getAccountNumber());
                if (!isAccountExist) {
                        return BankResponse.builder()
                                        .responsecode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
                                        .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                                        .accountInfo(null)
                                        .build();
                }
                User userToDebit = userRepository.findByAccountNumber(creditDebitRequest.getAccountNumber());
                if (userToDebit.getAccountBalance().compareTo(creditDebitRequest.getAmount()) < 0) {
                        return BankResponse.builder()
                                        .responsecode(AccountUtils.ACCOUNT_BALANCE_INSUFFICIENT_CODE)
                                        .responseMessage(AccountUtils.ACCOUNT_BALANCE_INSUFFICIENT_MESSAGE)
                                        .accountInfo(null)
                                        .build();
                }
                userToDebit.setAccountBalance(userToDebit.getAccountBalance().subtract(creditDebitRequest.getAmount()));
                userRepository.save(userToDebit);

                TransactionDTO transactionDTO = TransactionDTO.builder()
                                .transactionType("CREDIT")
                                .transactionAmount(creditDebitRequest.getAmount())
                                .accountNumber(userToDebit.getAccountNumber())
                                .transactionStatus("SUCCESS")
                                .build();

                transactionService.saveTransaction(transactionDTO);

                return BankResponse.builder()
                                .responsecode(AccountUtils.ACCOUNT_DEBITED_SUCCESS)
                                .responseMessage(AccountUtils.ACCOUNT_DEBITED_SUCCESS_MESSAGE)
                                .accountInfo(AccountInfo.builder()
                                                .accountBalance(userToDebit.getAccountBalance())
                                                .accountName(userToDebit.getFirstName() + " "
                                                                + userToDebit.getLastName() + " "
                                                                + userToDebit.getMiddleName())
                                                .accountNumber(userToDebit.getAccountNumber())
                                                .build())
                                .build();
        }

        @Override
        public BankResponse transferAmount(TransferRequest transferRequest) {
                boolean isDestinationAccountExist = userRepository
                                .existsByAccountNumber(transferRequest.getDestinationAccountNumber());
                if (!isDestinationAccountExist) {
                        return BankResponse.builder()
                                        .responsecode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
                                        .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                                        .accountInfo(null)
                                        .build();
                }
                User sourceAccountUser = userRepository.findByAccountNumber(transferRequest.getSourceAccountNumber());
                if (sourceAccountUser.getAccountBalance().compareTo(transferRequest.getAmount()) < 0) {
                        return BankResponse.builder()
                                        .responsecode(AccountUtils.ACCOUNT_BALANCE_INSUFFICIENT_CODE)
                                        .responseMessage(AccountUtils.ACCOUNT_BALANCE_INSUFFICIENT_MESSAGE)
                                        .accountInfo(null)
                                        .build();
                }
                sourceAccountUser.setAccountBalance(
                                sourceAccountUser.getAccountBalance().subtract(transferRequest.getAmount()));
                userRepository.save(sourceAccountUser);
                EmailDetails debitAlert = EmailDetails.builder()
                                .recipient(sourceAccountUser.getEmail())
                                .subject("DEBIT ALERT")
                                .messageBody("Your account has been debited with " + transferRequest.getAmount()
                                                + " on " + java.time.LocalDateTime.now())
                                .build();
                emailService.sendEmailAlert(debitAlert);

                User destinationAccountUser = userRepository
                                .findByAccountNumber(transferRequest.getDestinationAccountNumber());
                destinationAccountUser.setAccountBalance(
                                destinationAccountUser.getAccountBalance().add(transferRequest.getAmount()));
                userRepository.save(destinationAccountUser);
                EmailDetails creditAlert = EmailDetails.builder()
                                .recipient(destinationAccountUser.getEmail())
                                .subject("CREDIT ALERT")
                                .messageBody("Your account has been credited with " + transferRequest.getAmount()
                                                + " on " + java.time.LocalDateTime.now())
                                .build();
                emailService.sendEmailAlert(creditAlert);

                TransactionDTO transactionDTO = TransactionDTO.builder()
                                .transactionType("CREDIT")
                                .transactionAmount(transferRequest.getAmount())
                                .accountNumber(sourceAccountUser.getAccountNumber())
                                .transactionStatus("SUCCESS")
                                .build();

                transactionService.saveTransaction(transactionDTO);

                return BankResponse.builder()
                                .responsecode(AccountUtils.ACCOUNT_TRANSFER_SUCCESS_CODE)
                                .responseMessage(AccountUtils.ACCOUNT_TRANSFER_SUCCESS_MESSAGE)
                                .accountInfo(AccountInfo.builder()
                                                .accountBalance(sourceAccountUser.getAccountBalance())
                                                .accountName(sourceAccountUser.getFirstName() + " "
                                                                + sourceAccountUser.getLastName() + " "
                                                                + sourceAccountUser.getMiddleName())
                                                .accountNumber(sourceAccountUser.getAccountNumber())
                                                .build())
                                .build();
        }

}

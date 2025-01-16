package com.farhan.secure_banking_app.utils;

import java.time.Year;

public class AccountUtils {

    public static final String ACCOUNT_EXIST_CODE = "0001";
    public static final String ACCOUNT_EXIST_MESSAGE = "User already has an account";

    public static final String ACCOUNT_CREATION_SUCCESS = "0002";
    public static final String ACCOUNT_CREATION_MESSAGE = "Account has been successfully created";

    public static final String ACCOUNT_NOT_EXIST_CODE = "0003";
    public static final String ACCOUNT_NOT_EXIST_MESSAGE = "Account does not exist";

    public static final String ACCOUNT_FOUND_CODE = "0004";
    public static final String ACCOUNT_FOUND_MESSAGE = "Account found";

    public static final String ACCOUNT_CREDITED_SUCCESS = "0005";
    public static final String ACCOUNT_CREDITED_SUCCESS_MESSAGE = "Account has been credited";

    public static final String ACCOUNT_DEBITED_SUCCESS = "0006";
    public static final String ACCOUNT_DEBITED_SUCCESS_MESSAGE = "Account has been debited";

    public static final String ACCOUNT_BALANCE_INSUFFICIENT_CODE = "0007";
    public static final String ACCOUNT_BALANCE_INSUFFICIENT_MESSAGE = "Account balance is insufficient";

    public static final String ACCOUNT_TRANSFER_SUCCESS_CODE = "0008";
    public static final String ACCOUNT_TRANSFER_SUCCESS_MESSAGE = "Amount has been transferred";
    

    public static String generateAccountNumber() {
        /*
        * 2025 + randomSixDigitNumber
        */

        Year currentYear = Year.now();

        int min = 100000;
        int max = 999999;
        int randomNumber = (int) Math.floor(Math.random() * (max - min + 1) + min);
        String year = String.valueOf(currentYear);
        String randNumber = String.valueOf(randomNumber);
        StringBuilder accountNumber = new StringBuilder();

        accountNumber.append(year);
        accountNumber.append(randNumber);
        return accountNumber.toString();
    }
}

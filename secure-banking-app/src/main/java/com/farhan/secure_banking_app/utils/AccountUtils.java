package com.farhan.secure_banking_app.utils;

import java.time.Year;

public class AccountUtils {

    public static final String ACCOUNT_EXIST_CODE = "0001";
    public static final String ACCOUNT_EXIST_MESSAGE = "User already has an account";

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
        accountNumber.append(randomNumber);
        return accountNumber.toString();
    }
}

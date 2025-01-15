package com.farhan.secure_banking_app.services;

import com.farhan.secure_banking_app.dto.EmailDetails;

public interface EmailService {
    void sendEmailAlert(EmailDetails emailDetails);
}

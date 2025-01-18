package com.farhan.secure_banking_app.services;

import java.io.File;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.farhan.secure_banking_app.dto.EmailDetails;

import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String senderEmail;

    @Override
    public void sendEmailAlert(EmailDetails emailDetails) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(senderEmail);
            mailMessage.setTo(emailDetails.getRecipient());
            mailMessage.setSubject(emailDetails.getSubject());
            mailMessage.setText(emailDetails.getMessageBody());
            javaMailSender.send(mailMessage);
            System.out.println("Email sent successfully");
        } catch (Exception e) {
            throw new RuntimeException("Email sending failed");
        }
    }

    @Override
    public void sendEmailWithAttachment(EmailDetails emailDetails) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper;
        try {
            helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(senderEmail);
            helper.setTo(emailDetails.getRecipient());
            helper.setSubject(emailDetails.getSubject());
            helper.setText(emailDetails.getMessageBody());
            FileSystemResource file = new FileSystemResource(new File(emailDetails.getAttachment()));
            helper.addAttachment(Objects.requireNonNull(file.getFilename(), file));
            javaMailSender.send(mimeMessage);

            log.info(file.getFilename() + "has been sent to user with email " + emailDetails.getRecipient());

        } catch (Exception e) {
            throw new RuntimeException("Email sending failed", e);
        }
    }

}       

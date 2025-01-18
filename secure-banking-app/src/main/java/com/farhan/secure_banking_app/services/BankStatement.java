package com.farhan.secure_banking_app.services;

import java.util.List;
import java.io.FileOutputStream;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.farhan.secure_banking_app.dto.EmailDetails;
import com.farhan.secure_banking_app.entity.Transaction;
import com.farhan.secure_banking_app.entity.User;
import com.farhan.secure_banking_app.repo.TransactionRepo;
import com.farhan.secure_banking_app.repo.UserRepository;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@AllArgsConstructor
@Slf4j
public class BankStatement {

    @Autowired
    private TransactionRepo transactionRepo;

    @Autowired
    private UserRepository userRepository;

    private static final String FILE = "bank-statement.pdf";

    @Autowired
    private EmailService emailService;

    public List<Transaction> getTransactions(String accountNumber, String startDate, String endDate) {
        List<Transaction> transactionList = transactionRepo.findAll().stream()
                .filter(transaction -> transaction.getAccountNumber().equals(accountNumber))
                .filter(transaction -> transaction.getCreatedAt().isAfter(java.time.LocalDateTime.parse(startDate)))
                .filter(transaction -> transaction.getCreatedAt().isBefore(java.time.LocalDateTime.parse(endDate)))
                .toList();
        User user = userRepository.findByAccountNumber(accountNumber);
        if (user == null) {
            log.error("User not found for account number: " + accountNumber);
            return transactionList;
        }

        String customerName = user.getFirstName() + " " + user.getLastName();
        String customerAddress = user.getAddress();
        String customerEmail = user.getEmail();
        String customerPhoneNumber = user.getPhoneNumber();
        String customerAccountNumber = user.getAccountNumber();
        String customerAccountBalance = user.getAccountBalance().toString();
        String customerCountry = user.getCountry();
        String customerStatus = user.getStatus();

        // Design the bank statement
        Rectangle pageSize = new Rectangle(PageSize.A4);
        Document document = new Document(pageSize);
        try {
            PdfWriter.getInstance(document, new FileOutputStream(FILE));
            document.open();
            PdfPTable bankInfoTable = new PdfPTable(1);
            PdfPCell cell = new PdfPCell(new Paragraph("Bank Statement"));
            cell.setBorder(0);
            cell.setBackgroundColor(BaseColor.BLUE);
            cell.setPadding(20f);
            bankInfoTable.addCell(cell);
            document.add(bankInfoTable);

            PdfPTable transactionTable = new PdfPTable(4);

            PdfPCell customerInfo = new PdfPCell(new Phrase("Start date: " + startDate));
            customerInfo.setBorder(0);

            PdfPCell statement = new PdfPCell(new Phrase("STATEMENT OF ACCOUNT"));
            statement.setBorder(0);

            PdfPCell stopDate = new PdfPCell(new Phrase("End date: " + endDate));
            stopDate.setBorder(0);

            PdfPCell customerNameCell = new PdfPCell(new Phrase("Customer Name: " + customerName));
            customerNameCell.setBorder(0);

            PdfPCell accountNumberCell = new PdfPCell(new Phrase("Account Number: " + customerAccountNumber));
            accountNumberCell.setBorder(0);

            PdfPCell accountBalanceCell = new PdfPCell(new Phrase("Account Balance: " + customerAccountBalance));
            accountBalanceCell.setBorder(0);

            PdfPCell customerAddressCell = new PdfPCell(new Phrase("Address: " + customerAddress));
            customerAddressCell.setBorder(0);

            PdfPCell customerEmailCell = new PdfPCell(new Phrase("Email: " + customerEmail));
            customerEmailCell.setBorder(0);

            PdfPCell customerPhoneNumberCell = new PdfPCell(new Phrase("Phone Number: " + customerPhoneNumber));
            customerPhoneNumberCell.setBorder(0);

            PdfPCell customerCountryCell = new PdfPCell(new Phrase("Country: " + customerCountry));
            customerCountryCell.setBorder(0);

            PdfPCell customerStatusCell = new PdfPCell(new Phrase("Status: " + customerStatus));
            customerStatusCell.setBorder(0);

            PdfPTable transactionsTable = new PdfPTable(4);
            PdfPCell date = new PdfPCell(new Phrase("Date"));
            date.setBackgroundColor(BaseColor.LIGHT_GRAY);
            date.setBorder(0);

            PdfPCell transactionIdCell = new PdfPCell(new Phrase("Transaction ID"));
            transactionIdCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            transactionIdCell.setBorder(0);

            PdfPCell transactionTypeCell = new PdfPCell(new Phrase("Transaction Type"));
            transactionTypeCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            transactionTypeCell.setBorder(0);

            PdfPCell transactionAmountCell = new PdfPCell(new Phrase("Transaction Amount"));
            transactionAmountCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            transactionAmountCell.setBorder(0);

            PdfPCell transactionStatusCell = new PdfPCell(new Phrase("Transaction Status"));
            transactionStatusCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            transactionStatusCell.setBorder(0);

            transactionsTable.addCell(date);
            transactionsTable.addCell(transactionIdCell);
            transactionsTable.addCell(transactionTypeCell);
            transactionsTable.addCell(transactionAmountCell);
            transactionsTable.addCell(transactionStatusCell);

            for (Transaction transaction : transactionList) {
                PdfPCell dateCell = new PdfPCell(new Phrase(transaction.getCreatedAt().toString()));
                dateCell.setBorder(0);

                PdfPCell transactionIdValueCell = new PdfPCell(new Phrase(transaction.getTransactionId().toString()));
                transactionIdValueCell.setBorder(0);

                PdfPCell transactionTypeValueCell = new PdfPCell(new Phrase(transaction.getTransactionType()));
                transactionTypeValueCell.setBorder(0);

                PdfPCell transactionAmountValueCell = new PdfPCell(new Phrase(transaction.getAmount().toString()));
                transactionAmountValueCell.setBorder(0);

                PdfPCell transactionStatusValueCell = new PdfPCell(new Phrase(transaction.getStatus()));
                transactionStatusValueCell.setBorder(0);

                transactionsTable.addCell(dateCell);
                transactionsTable.addCell(transactionIdValueCell);
                transactionsTable.addCell(transactionTypeValueCell);
                transactionsTable.addCell(transactionAmountValueCell);
                transactionsTable.addCell(transactionStatusValueCell);
            }

            document.add(transactionsTable);
        } catch (Exception e) {
            log.error("Error creating PDF document", e);
        } finally {
            document.close();
        }

        EmailDetails emailDetails = EmailDetails.builder()
            .recipient(user.getEmail())
            .subject("STATEMENT OF ACCOUNT")
            .messageBody("Kindly find your requested account statement attached")
            .attachment(FILE)
            .build();

        emailService.sendEmailWithAttachment(emailDetails);

        return transactionList;
    }
}
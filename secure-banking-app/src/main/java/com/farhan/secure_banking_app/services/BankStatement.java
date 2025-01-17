package com.farhan.secure_banking_app.services;

import java.util.List;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;

import org.springframework.stereotype.Component;

import com.farhan.secure_banking_app.entity.Transaction;
import com.farhan.secure_banking_app.entity.User;
import com.farhan.secure_banking_app.repo.TransactionRepo;
import com.farhan.secure_banking_app.repo.UserRepository;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j

public class BankStatement {
    
    private TransactionRepo transactionRepo;
    private UserRepository userRepository;

    private static final String FILE = "bank-statement.pdf";


    public List<Transaction> getTransactions(String accountNumber, String startDate, String endDate) {
        List<Transaction> transactionList = transactionRepo.findAll().stream()
                .filter(transaction -> transaction.getAccountNumber().equals(accountNumber))
                .filter(transaction -> transaction.getCreatedAt().isAfter(java.time.LocalDateTime.parse(startDate)))
                .filter(transaction -> transaction.getCreatedAt().isBefore(java.time.LocalDateTime.parse(endDate)))
                .toList();

        // Design the bank statement
        Rectangle pageSize = new Rectangle(PageSize.A4);
        Document document = new Document(pageSize);
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
        
        PdfPCell customerInfo = new PdfPCell(new Phrase("start date: " + startDate));
        customerInfo.setBorder(0);
        PdfPCell statement = new PdfPCell(new Phrase("STATEMENT OF ACCOUNT"));
        statement.setBorder(0);
        PdfPCell stopDate = new PdfPCell(new Phrase("end date: " + endDate));
        stopDate.setBorder(0);

        return transactionList;

       

    private void designStatement(List<Transaction> transactionList) throws DocumentException, FileNotFoundException {
        

        System.out.println("Bank statement generated successfully");

    }
}

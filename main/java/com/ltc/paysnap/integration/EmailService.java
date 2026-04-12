package com.ltc.paysnap.integration;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;
import java.io.File;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendReceipt(String toEmail, String filePath) throws Exception {

        MimeMessage message = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(toEmail);
        helper.setSubject("PaySnap Receipt");
        helper.setText("Your payment was successful. Receipt attached.");

        FileSystemResource file = new FileSystemResource(new File(filePath));
        helper.addAttachment("receipt.pdf", file);

        mailSender.send(message);
    }
}
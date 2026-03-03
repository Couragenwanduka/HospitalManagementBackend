package com.hospitalManagement.demo.utils;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailSender {

    @Value("${GoogleEmail}")
    private String username;

    @Value("${GooglePassword}")
    private String password;

    
    public void sendEmail(String recipientEmail, String subject, String body) {

        // 1. SMTP server configuration
        String host = "smtp.gmail.com"; // Gmail SMTP server
        String port = "587"; // TLS port


        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true"); // TLS
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);

        // 2. Create session
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            // 3. Create message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(
                    Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject(subject  != null ? subject : "Test Email from Java");
            message.setText(body != null ? body : "Hello! This is a test email sent from Java.");

            // 4. Send message
            Transport.send(message);

            System.out.println("Email sent successfully!");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
package com.Lucifer.AuthApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendWelcomeEmail(String toEmail, String name) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setTo(toEmail);
        mailMessage.setSubject("Employee Registration Confirmation");
        mailMessage.setText("welcome"+name+" \"Your registration has been completed successfully.");
        mailSender.send(mailMessage);
    }
    public void sendResetOtpEmail(String toEmail, String otp) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(toEmail);
        mailMessage.setSubject("Password Reset OTP - By Hamster");
        String message = """
            Hello,
            We received a request to reset your password.
            Your Password Reset OTP is:

            🔐 %s

            This OTP is valid for a limited time. Please do not share this OTP
            with anyone.

            If you did not request a password reset, please ignore this email.

            Regards,
            Your Application Team
            """.formatted(otp);

        mailMessage.setText(message);

        mailSender.send(mailMessage);
    }
}

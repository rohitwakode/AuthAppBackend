package com.Lucifer.AuthApp.authentication;

import com.Lucifer.AuthApp.model.Student;
import com.Lucifer.AuthApp.repo.StudentRepo;
import com.Lucifer.AuthApp.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class AuthService {

    @Autowired
    private StudentRepo studentRepo;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    //send otp to email
    public void sendResetOtp(String email) {
        Student student = studentRepo.findByEmail(email);
        if(student == null){
            throw new RuntimeException("email not exist");
        }
        String otp = String.valueOf(
                ThreadLocalRandom.current().nextInt(100000, 1000000)
        );
        Long  expTime = System.currentTimeMillis() + ( 1* 60 * 1000L);

        student.setResetOtp(otp);
        student.setResetOtpExpiredAt(expTime);

        studentRepo.save(student);

        emailService.sendResetOtpEmail(student.getEmail(), otp);
    }



    //Genrating Token
    private String generateResetToken() {

        SecureRandom secureRandom = new SecureRandom();

        byte[] tokenBytes = new byte[32];

        secureRandom.nextBytes(tokenBytes);

        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(tokenBytes);
    }

    //Verify Otp
    public String verifyOtp(String email,String otp) {
        Student student = studentRepo.findByEmail(email);
        if(student == null){
            throw new RuntimeException("email not exist");
        }
        if (student.getResetOtp() == null) {
            throw new RuntimeException("OTP not generated");
        }

        if (!student.getResetOtp().equals(otp)) {
            throw new RuntimeException("Invalid OTP");
        }

        if (student.getResetOtpExpiredAt() == null ||
                student.getResetOtpExpiredAt() < System.currentTimeMillis()) {

            throw new RuntimeException("OTP expired");
        }
        // OTP is correct
        String resetToken = generateResetToken();

        // Token valid for 10 minutes
        Long tokenExpiry =
                System.currentTimeMillis() + (10 * 60 * 1000L);

        student.setResetToken(resetToken);
        student.setResetTokenExpiredAt(tokenExpiry);

        // OTP should no longer be usable
        //that mean after the operation value of otp and time  will be null
        student.setResetOtp(null);
        student.setResetOtpExpiredAt(null);

        studentRepo.save(student);

        return resetToken;
    }
    public void resetPassword(ResetPassReq resetPassReq) {

        Student student = studentRepo.findByResetToken(resetPassReq.resetToken());

        if (student == null) {
            throw new RuntimeException("Invalid reset token");
        }

        if (student.getResetTokenExpiredAt() == null ||
                student.getResetTokenExpiredAt() < System.currentTimeMillis()) {

            throw new RuntimeException("Reset token expired");
        }

        student.setPassword(passwordEncoder.encode(resetPassReq.newPassword()));

        // Token can be used only once
        //it will get null after operation successfull completed
        student.setResetToken(null);
        student.setResetTokenExpiredAt(null);

        studentRepo.save(student);
    }



}

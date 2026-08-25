package com.Lucifer.AuthApp.authentication;

import com.Lucifer.AuthApp.dtos.*;
import com.Lucifer.AuthApp.exception.InvalidCredentials;
import com.Lucifer.AuthApp.security.JwtService;
import com.Lucifer.AuthApp.security.Token;
import com.Lucifer.AuthApp.service.EmailService;
import com.Lucifer.AuthApp.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private AuthService authService;

    @Autowired
    private EmailService emailService;


    @PostMapping("/login")
    public Token login(@RequestBody Login login) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(login.email(), login.password()));
        if (authentication.isAuthenticated()) {
            String token= jwtService.generateToken(login.email());
            return new Token(token);
        }
        else  {
            throw new InvalidCredentials("Invalid email or password");
        }
    }
    @PostMapping("/save")
    ResponseEntity<StdResDto> addStudent(@RequestBody StdReqDto student){
        emailService.sendWelcomeEmail(student.email(),student.name());
        return new ResponseEntity<>(studentService.savedStudent(student), HttpStatus.CREATED);
    }


    @GetMapping("isAuth")
    public Boolean isAuthenticated(Authentication auth) {
        String email = auth.getName();
        if (email != null) {
            return true;
        }
        return false;
    }

    @PostMapping("/resetotp")
    public void sendResetOtpEmail(@RequestParam String email) {
       authService.sendResetOtp(email);
    }

    @PostMapping("/verifyotp")
    public String verifyResetOtp(
            @RequestBody OtpReq request) {

        return authService.verifyOtp(
                request.email(),
                request.otp()
        );
    }
    @PostMapping("/resetpassword")
    public String resetPassword(
            @RequestBody ResetPassReq request) {

        authService.resetPassword(
               request
        );

        return "Password reset successfully";
    }


}

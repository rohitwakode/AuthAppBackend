package com.Lucifer.AuthApp.authentication;

public record OtpReq(
        String email,
        String otp
) {
}

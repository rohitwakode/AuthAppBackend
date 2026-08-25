package com.Lucifer.AuthApp.authentication;

public record ResetPassReq(
        String resetToken,
        String newPassword
) {
}

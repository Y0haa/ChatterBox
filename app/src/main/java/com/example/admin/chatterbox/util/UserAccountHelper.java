package com.example.admin.chatterbox.util;

/**
 * Created by Admin on 10/12/2017.
 */

public class UserAccountHelper {

    public static boolean isEmailValid(String email) {
        return email.contains("@");
    }

    public static boolean isPasswordValid(String confirmPassword, String password) {
        return confirmPassword.equals(password) && password.length() > 4;
    }
}

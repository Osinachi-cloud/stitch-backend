package com.stitch.user.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class UserValidationUtils {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+?[1-9]\\d{1,14}$");
    private static final int MIN_PASSWORD_LENGTH = 8;
    private static final int MAX_PASSWORD_LENGTH = 64;
    private static final Pattern PASSWORD_UPPERCASE_PATTERN = Pattern.compile("^(?=.*[A-Z]).*$");
    private static final Pattern PASSWORD_DIGIT_PATTERN = Pattern.compile("^(?=.*[0-9]).*$");
    private static final Pattern PASSWORD_SPECIAL_CHARACTER_PATTERN = Pattern.compile("^(?=.*[^a-zA-Z0-9]).*$");

    public static boolean isValidEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }

    public static boolean isValidPhoneNumber(String phoneNumber) {
        return PHONE_PATTERN.matcher(phoneNumber).matches();
    }

    public static List<String> getPasswordRequirements() {
        List<String> requirements = new ArrayList<>();
        requirements.add("Must be between " + MIN_PASSWORD_LENGTH + " and " + MAX_PASSWORD_LENGTH + " characters in length.");
        requirements.add("Must contain at least one uppercase letter.");
        requirements.add("Must contain at least one digit.");
        requirements.add("Must contain at least one special character.");
        return requirements;
    }

    public static List<String> getPasswordErrors(String password) {
        List<String> errors = new ArrayList<>();
        if (password.length() < MIN_PASSWORD_LENGTH || password.length() > MAX_PASSWORD_LENGTH) {
            errors.add("Password must be between " + MIN_PASSWORD_LENGTH + " and " + MAX_PASSWORD_LENGTH + " characters in length.");
        }
        if (!PASSWORD_UPPERCASE_PATTERN.matcher(password).matches()) {
            errors.add("Password must contain at least one uppercase letter.");
        }
        if (!PASSWORD_DIGIT_PATTERN.matcher(password).matches()) {
            errors.add("Password must contain at least one digit.");
        }
        if (!PASSWORD_SPECIAL_CHARACTER_PATTERN.matcher(password).matches()) {
            errors.add("Password must contain at least one special character.");
        }
        return errors;
    }
}
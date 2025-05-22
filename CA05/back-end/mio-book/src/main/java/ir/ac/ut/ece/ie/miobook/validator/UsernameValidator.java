package ir.ac.ut.ece.ie.miobook.validator;

import java.util.regex.Pattern;

public class UsernameValidator {

    private static final Pattern USERNAME_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9_-]+$"
    );

    public static boolean isValid(String username) {
        if (username == null || username.isEmpty()) {
            return false;
        }
        return USERNAME_PATTERN.matcher(username).matches();
    }
}
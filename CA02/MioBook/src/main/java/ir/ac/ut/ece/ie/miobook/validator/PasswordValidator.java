package ir.ac.ut.ece.ie.miobook.validator;

public class PasswordValidator {

    public static boolean isValid(String password) {
        if (password == null) {
            return false;
        }

        if (password.length() < 4) {
            return false;
        }

        return true;
    }
}

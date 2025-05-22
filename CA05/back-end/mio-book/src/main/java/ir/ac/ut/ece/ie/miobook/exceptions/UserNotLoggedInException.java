package ir.ac.ut.ece.ie.miobook.exceptions;

public class UserNotLoggedInException extends RuntimeException {

    private static final String ERROR_CODE = "USER_NOT_LOGGED_IN";

    public UserNotLoggedInException() {
        super(String.format("Error [%s]: No user logged in now.", ERROR_CODE));
    }

    public String getErrorCode() {
        return ERROR_CODE;
    }
}

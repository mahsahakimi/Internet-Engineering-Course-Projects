package ir.ac.ut.ece.ie.miobook.exceptions;

public class UserSessionActiveException extends RuntimeException {

    private static final String ERROR_CODE = "USER_SESSION_ACTIVE";

    public UserSessionActiveException() {
        super(String.format("Error [%s]: A user is already logged in.", ERROR_CODE));
    }

    public String getErrorCode() {
        return ERROR_CODE;
    }
}
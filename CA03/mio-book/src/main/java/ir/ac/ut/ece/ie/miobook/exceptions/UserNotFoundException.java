package ir.ac.ut.ece.ie.miobook.exceptions;

public class UserNotFoundException extends RuntimeException {

    private static final String ERROR_CODE = "USER_NOT_FOUND";

    public UserNotFoundException(String username) {
        super(String.format("Error [%s]: User %s not found.", ERROR_CODE, username));
    }

    public String getErrorCode() {
        return ERROR_CODE;
    }
}

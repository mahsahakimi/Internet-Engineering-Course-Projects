package ir.ac.ut.ece.ie.miobook.exceptions;

public class WalletNotFoundException extends RuntimeException {
    private static final String ERROR_CODE = "USER_NOT_LOGGED_IN";

    public WalletNotFoundException(String username) {
        super(String.format("Error [%s]: wallet not found for user : '%s'", ERROR_CODE, username));
    }

    public String getErrorCode() {
        return ERROR_CODE;
    }
}

package ir.ac.ut.ece.ie.miobook.exceptions;

public class InvalidEmailException extends RuntimeException {

    private static final String ERROR_CODE = "INVALID_EMAIL";

    public InvalidEmailException(String email) {
        super(String.format("Error [%s]: Email is not valid: %s", ERROR_CODE, email));
    }

    public String getErrorCode() {
        return ERROR_CODE;
    }
}
package ir.ac.ut.ece.ie.miobook.exceptions;

public class EmailAlreadyExistsException extends RuntimeException {

    private static final String ERROR_CODE = "EMAIL_DUPLICATE";

    public EmailAlreadyExistsException(String email) {
        super(String.format("Error [%s]: Email '%s' is already in use.", ERROR_CODE, email));
    }

    public String getErrorCode() {
        return ERROR_CODE;
    }
}
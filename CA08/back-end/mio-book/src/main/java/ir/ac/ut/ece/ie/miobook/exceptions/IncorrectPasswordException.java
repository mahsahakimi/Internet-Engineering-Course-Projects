package ir.ac.ut.ece.ie.miobook.exceptions;

public class IncorrectPasswordException extends RuntimeException {

    private static final String ERROR_CODE = "INCORRECT_PASSWORD";

    public IncorrectPasswordException() {
        super(String.format("Error [%s]: The provided password is incorrect.", ERROR_CODE));
    }

    public String getErrorCode() {
        return ERROR_CODE;
    }
}

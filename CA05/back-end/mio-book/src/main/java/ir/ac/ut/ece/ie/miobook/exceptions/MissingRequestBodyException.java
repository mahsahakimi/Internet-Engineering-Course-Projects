package ir.ac.ut.ece.ie.miobook.exceptions;

public class MissingRequestBodyException extends RuntimeException {

    private static final String ERROR_CODE = "MISSING_REQUEST_BODY";

    public MissingRequestBodyException() {
        super(String.format("Error [%s]: Request body is missing.", ERROR_CODE));
    }

    public String getErrorCode() {
        return ERROR_CODE;
    }
}
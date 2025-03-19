package ir.ac.ut.ece.ie.miobook.exceptions;

public class InvalidYearException extends RuntimeException {

    private static final String ERROR_CODE = "INVALID_YEAR";

    public InvalidYearException() {
        super(String.format("Error [%s]: Year must be a positive number.", ERROR_CODE));
    }

    public String getErrorCode() {
        return ERROR_CODE;
    }
}
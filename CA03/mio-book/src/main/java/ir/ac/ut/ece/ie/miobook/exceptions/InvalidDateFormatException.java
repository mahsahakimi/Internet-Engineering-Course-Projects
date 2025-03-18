package ir.ac.ut.ece.ie.miobook.exceptions;

public class InvalidDateFormatException extends RuntimeException {

    private static final String ERROR_CODE = "INVALID_DATE_FORMAT";

    public InvalidDateFormatException() {
        super(String.format("Error [%s]: Please enter the date in the format YYYY-MM-DD (e.g., 2023-12-25).", ERROR_CODE));
    }

    public String getErrorCode() {
        return ERROR_CODE;
    }
}
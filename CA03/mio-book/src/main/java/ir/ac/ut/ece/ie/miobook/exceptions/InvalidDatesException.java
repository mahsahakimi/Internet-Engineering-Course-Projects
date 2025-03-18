package ir.ac.ut.ece.ie.miobook.exceptions;

public class InvalidDatesException extends RuntimeException {

    private static final String ERROR_CODE = "INVALID_DATES";

    public InvalidDatesException() {
        super(String.format("Error [%s]: The dates you entered are not valid. Please check the date ranges.", ERROR_CODE));
    }

    public String getErrorCode() {
        return ERROR_CODE;
    }
}
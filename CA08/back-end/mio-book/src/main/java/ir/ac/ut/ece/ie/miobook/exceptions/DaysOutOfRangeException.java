package ir.ac.ut.ece.ie.miobook.exceptions;

public class DaysOutOfRangeException extends RuntimeException {

    private static final String ERROR_CODE = "DAYS_OUT_OF_RANGE";

    public DaysOutOfRangeException() {
        super(String.format("Error [%s]: Invalid number of days. Days must be between 1 and 9.", ERROR_CODE));
    }

    public String getErrorCode() {
        return ERROR_CODE;
    }
}
package ir.ac.ut.ece.ie.miobook.exceptions;

public class InvalidYearRangeException extends RuntimeException {

    private static final String ERROR_CODE = "INVALID_YEAR_RANGE";

    public InvalidYearRangeException() {
        super(String.format("Error [%s]: Invalid year range. 'From' year cannot be greater than 'To' year.", ERROR_CODE));
    }

    public String getErrorCode() {
        return ERROR_CODE;
    }
}

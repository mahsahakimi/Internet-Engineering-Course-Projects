package ir.ac.ut.ece.ie.miobook.exceptions;

    public class InvalidRatingException extends RuntimeException {

    private static final String ERROR_CODE = "INVALID_RATING";

    public InvalidRatingException() {
        super(String.format("Error [%s]: Rate must be an integer between 1 and 5.", ERROR_CODE));
    }

    public String getErrorCode() {
        return ERROR_CODE;
    }
}

package ir.ac.ut.ece.ie.miobook.exceptions;

public class NoReviewsException extends RuntimeException {

    private static final String ERROR_CODE = "NO_REVIEWS";

    public NoReviewsException(String title) {
        super(String.format("Error [%s]: There are no reviews for the book '%s'.", ERROR_CODE, title));
    }

    public String getErrorCode() {
        return ERROR_CODE;
    }
}

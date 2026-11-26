package ir.ac.ut.ece.ie.miobook.exceptions;

public class ReviewAlreadyExistsException extends RuntimeException {

    private static final String ERROR_CODE = "REVIEW_ALREADY_EXISTS";

    public ReviewAlreadyExistsException(String bookTitle) {
        super(String.format("Error [%s]: You have already submitted a review for book '%s'.", ERROR_CODE, bookTitle));
    }

    public String getErrorCode() {
        return ERROR_CODE;
    }
}


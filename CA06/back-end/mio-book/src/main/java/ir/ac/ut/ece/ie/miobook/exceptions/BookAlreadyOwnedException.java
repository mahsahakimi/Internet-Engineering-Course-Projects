package ir.ac.ut.ece.ie.miobook.exceptions;

public class BookAlreadyOwnedException extends RuntimeException {

    private static final String ERROR_CODE = "BOOK_ALREADY_OWNED";

    public BookAlreadyOwnedException(String book) {
        super(String.format("Error [%s]: Book '%s' is already owned.", ERROR_CODE, book));
    }

    public String getErrorCode() {
        return ERROR_CODE;
    }
}
package ir.ac.ut.ece.ie.miobook.exceptions;

public class BookAccessDeniedException extends RuntimeException {

    private static final String ERROR_CODE = "BOOK_ACCESS_DENIED";

    public BookAccessDeniedException() {
        super(String.format("Error [%s]: User does not have access to the book!", ERROR_CODE));
    }

    public String getErrorCode() {
        return ERROR_CODE;
    }
}

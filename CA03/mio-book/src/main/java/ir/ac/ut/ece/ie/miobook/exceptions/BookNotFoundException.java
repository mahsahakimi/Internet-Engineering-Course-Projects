package ir.ac.ut.ece.ie.miobook.exceptions;

public class BookNotFoundException extends RuntimeException {

    private static final String ERROR_CODE = "BOOK_NOT_FOUND";

    public BookNotFoundException(String book) {
        super(String.format("Error [%s]: Book '%s' not found. Please check the book title and try again.", ERROR_CODE, book));
    }

    public String getErrorCode() {
        return ERROR_CODE;
    }
}
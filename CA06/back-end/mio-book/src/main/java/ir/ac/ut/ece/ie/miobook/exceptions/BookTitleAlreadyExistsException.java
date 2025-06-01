package ir.ac.ut.ece.ie.miobook.exceptions;

public class BookTitleAlreadyExistsException extends RuntimeException {

    private static final String ERROR_CODE = "BOOK_TITLE_DUPLICATE";

    public BookTitleAlreadyExistsException(String title) {
        super(String.format("Error [%s]: Book title '%s' already exists in the database.", ERROR_CODE, title));
    }

    public String getErrorCode() {
        return ERROR_CODE;
    }
}
package ir.ac.ut.ece.ie.miobook.exceptions;

public class BookAlreadyBorrowedException extends RuntimeException {

    private static final String ERROR_CODE = "BOOK_ALREADY_BORROWED";

    public BookAlreadyBorrowedException(String book) {
        super(String.format("Error [%s]: Book '%s' is already borrowed.", ERROR_CODE, book));
    }

    public String getErrorCode() {
        return ERROR_CODE;
    }
}
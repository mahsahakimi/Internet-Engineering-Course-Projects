package ir.ac.ut.ece.ie.miobook.exceptions;

public class BookAlreadyInCartException extends RuntimeException {

    private static final String ERROR_CODE = "BOOK_ALREADY_IN_CART";

    public BookAlreadyInCartException(String book) {
        super(String.format("Error [%s]: Book '%s' is already in the cart.", ERROR_CODE, book));
    }

    public String getErrorCode() {
        return ERROR_CODE;
    }
}
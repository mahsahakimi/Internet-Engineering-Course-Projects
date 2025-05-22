package ir.ac.ut.ece.ie.miobook.exceptions;

public class NoBooksFoundException extends RuntimeException {

    private static final String ERROR_CODE = "NO_BOOKS_FOUND";

    public NoBooksFoundException() {
        super(String.format("Error [%s]: No books found.", ERROR_CODE));
    }

    public String getErrorCode() {
        return ERROR_CODE;
    }
}

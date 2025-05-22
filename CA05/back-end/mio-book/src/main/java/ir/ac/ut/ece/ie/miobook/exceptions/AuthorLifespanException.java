package ir.ac.ut.ece.ie.miobook.exceptions;

public class AuthorLifespanException extends RuntimeException {

    private static final String ERROR_CODE = "AUTHOR_LIFESPAN_ERROR";

    public AuthorLifespanException() {
        super(String.format("Error [%s]: The author was not alive during the year the book was written.", ERROR_CODE));
    }

    public String getErrorCode() {
        return ERROR_CODE;
    }
}

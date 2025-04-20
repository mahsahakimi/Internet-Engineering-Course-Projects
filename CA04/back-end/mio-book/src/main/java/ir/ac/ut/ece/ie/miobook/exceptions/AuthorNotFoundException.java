package ir.ac.ut.ece.ie.miobook.exceptions;

public class AuthorNotFoundException extends RuntimeException {

    private static final String ERROR_CODE = "AUTHOR_NOT_FOUND";

    public AuthorNotFoundException(String authorName) {
        super(String.format("Error [%s]: Author '%s' could not be found. Please check the author's name and try again.", ERROR_CODE, authorName));
    }

    public String getErrorCode() {
        return ERROR_CODE;
    }
}
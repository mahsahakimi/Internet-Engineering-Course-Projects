package ir.ac.ut.ece.ie.miobook.exceptions;

public class AuthorAlreadyExistsException extends RuntimeException {

    private static final String ERROR_CODE = "AUTHOR_DUPLICATE";

    public AuthorAlreadyExistsException(String authorName) {
        super(String.format("Error [%s]: Author '%s' already exists in the database.", ERROR_CODE, authorName));
    }

    public String getErrorCode() {
        return ERROR_CODE;
    }
}
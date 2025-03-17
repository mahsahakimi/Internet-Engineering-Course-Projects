package ir.ac.ut.ece.ie.miobook.exceptions;

public class AuthorNotFoundException extends RuntimeException {

    public AuthorNotFoundException(String authorName) {
        super("Author '" + authorName + "' could not be found. Please check the author's name and try again.");
    }
}
package ir.ac.ut.ece.ie.miobook.exceptions;

public class BookNotFoundException extends RuntimeException {
    public BookNotFoundException(String book) {
        super("Book " + book + " not found.");
    }
}
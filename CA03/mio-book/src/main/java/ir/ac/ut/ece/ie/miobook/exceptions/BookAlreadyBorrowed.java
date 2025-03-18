package ir.ac.ut.ece.ie.miobook.exceptions;

public class BookAlreadyBorrowed extends RuntimeException {
    public BookAlreadyBorrowed(String book) {
        super("Book " + book + " is already borrowed.");
    }
}

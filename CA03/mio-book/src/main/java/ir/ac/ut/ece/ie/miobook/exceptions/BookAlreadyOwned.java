package ir.ac.ut.ece.ie.miobook.exceptions;

public class BookAlreadyOwned extends RuntimeException {
    public BookAlreadyOwned(String book) {
        super("Book " + book + " is already owned.");
    }
}

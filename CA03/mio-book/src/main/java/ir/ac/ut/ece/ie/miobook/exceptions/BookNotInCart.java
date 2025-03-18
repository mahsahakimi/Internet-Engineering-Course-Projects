package ir.ac.ut.ece.ie.miobook.exceptions;

public class BookNotInCart extends RuntimeException {
    public BookNotInCart(String book) {
        super("book " + book + " is not in cart.");
    }
}

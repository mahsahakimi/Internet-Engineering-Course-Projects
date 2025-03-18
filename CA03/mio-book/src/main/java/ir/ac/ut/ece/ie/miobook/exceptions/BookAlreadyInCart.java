package ir.ac.ut.ece.ie.miobook.exceptions;

public class BookAlreadyInCart extends RuntimeException {
    public BookAlreadyInCart(String book) {
        super("Book " + book + "is already in the cart.");
    }
}

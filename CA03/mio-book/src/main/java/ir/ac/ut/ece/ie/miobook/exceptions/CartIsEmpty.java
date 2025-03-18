package ir.ac.ut.ece.ie.miobook.exceptions;

public class CartIsEmpty extends RuntimeException {
    public CartIsEmpty() {
        super("The cart is empty!");
    }
}

package ir.ac.ut.ece.ie.miobook.exceptions;

public class CartIsFull extends RuntimeException {
    public CartIsFull() {
        super("The cart is full. It already contains 9 items.");
    }
}

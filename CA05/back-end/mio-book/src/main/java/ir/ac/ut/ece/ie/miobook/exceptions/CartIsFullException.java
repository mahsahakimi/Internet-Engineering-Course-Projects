package ir.ac.ut.ece.ie.miobook.exceptions;

public class CartIsFullException extends RuntimeException {

    private static final String ERROR_CODE = "CART_IS_FULL";

    public CartIsFullException() {
        super(String.format("Error [%s]: The cart is full. It already contains 9 items.", ERROR_CODE));
    }

    public String getErrorCode() {
        return ERROR_CODE;
    }
}

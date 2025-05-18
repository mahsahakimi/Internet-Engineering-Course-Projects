package ir.ac.ut.ece.ie.miobook.exceptions;

public class CartIsEmptyException extends RuntimeException {

    private static final String ERROR_CODE = "CART_IS_EMPTY";

    public CartIsEmptyException() {
        super(String.format("Error [%s]: The cart is empty!", ERROR_CODE));
    }

    public String getErrorCode() {
        return ERROR_CODE;
    }
}
package ir.ac.ut.ece.ie.miobook.exceptions;

public class InvalidPriceTypeException extends RuntimeException {

    private static final String ERROR_CODE = "INVALID_PRICE_TYPE";

    public InvalidPriceTypeException(String price) {
        super(String.format("Error [%s]: The price must be of type double or int. Invalid price type: %s", ERROR_CODE, price));
    }

    public String getErrorCode() {
        return ERROR_CODE;
    }
}

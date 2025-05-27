package ir.ac.ut.ece.ie.miobook.exceptions;

public class InvalidPriceValueException extends RuntimeException {

    private static final String ERROR_CODE = "INVALID_PRICE";

    public InvalidPriceValueException() {
        super(String.format("Error [%s]: Price value can not be negative", ERROR_CODE));
    }

    public String getErrorCode() {
        return ERROR_CODE;
    }
}
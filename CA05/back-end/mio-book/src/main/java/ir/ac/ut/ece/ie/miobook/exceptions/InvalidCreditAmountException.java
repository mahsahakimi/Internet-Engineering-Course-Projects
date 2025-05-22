package ir.ac.ut.ece.ie.miobook.exceptions;

public class InvalidCreditAmountException extends RuntimeException {

    private static final String ERROR_CODE = "INVALID_CREDIT_AMOUNT";

    public InvalidCreditAmountException() {
        super(String.format("Error [%s]: Credit must be greater than zero.", ERROR_CODE));
    }

    public String getErrorCode() {
        return ERROR_CODE;
    }
}
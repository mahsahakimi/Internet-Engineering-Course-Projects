package ir.ac.ut.ece.ie.miobook.exceptions;

public class InsufficientBalanceException extends RuntimeException {

    private static final String ERROR_CODE = "INSUFFICIENT_BALANCE";

    public InsufficientBalanceException(String user) {
        super(String.format("Error [%s]: Payment declined: %s's current balance is insufficient.", ERROR_CODE, user));
    }

    public String getErrorCode() {
        return ERROR_CODE;
    }
}
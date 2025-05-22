package ir.ac.ut.ece.ie.miobook.exceptions;

public class MinimumCreditException extends RuntimeException {

    private static final String ERROR_CODE = "MINIMUM_CREDIT";

    public MinimumCreditException() {
        super(String.format("Error [%s]: Minimum credit is 100 cents (1 dollar).", ERROR_CODE));
    }

    public String getErrorCode() {
        return ERROR_CODE;
    }
}
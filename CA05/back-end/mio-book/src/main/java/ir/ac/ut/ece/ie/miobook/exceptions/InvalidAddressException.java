package ir.ac.ut.ece.ie.miobook.exceptions;

public class InvalidAddressException extends RuntimeException {

    private static final String ERROR_CODE = "INVALID_ADDRESS";

    public InvalidAddressException(String country, String city) {
        super(String.format("Error [%s]: Address is not valid: %s, %s", ERROR_CODE, country, city));
    }

    public String getErrorCode() {
        return ERROR_CODE;
    }
}
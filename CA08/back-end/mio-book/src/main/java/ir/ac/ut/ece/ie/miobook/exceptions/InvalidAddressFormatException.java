package ir.ac.ut.ece.ie.miobook.exceptions;

public class InvalidAddressFormatException extends RuntimeException {

    private static final String ERROR_CODE = "INVALID_ADDRESS_FORMAT";

    public InvalidAddressFormatException() {
        super(String.format("Error [%s]: Invalid address format. Please check the address fields.", ERROR_CODE));
    }

    public String getErrorCode() {
        return ERROR_CODE;
    }
}
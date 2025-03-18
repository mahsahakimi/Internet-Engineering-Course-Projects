package ir.ac.ut.ece.ie.miobook.exceptions;

public class InvalidJsonFormatException extends RuntimeException {

    private static final String ERROR_CODE = "INVALID_JSON_FORMAT";

    public InvalidJsonFormatException() {
        super(String.format("Error [%s]: Invalid JSON format. Please check your request and ensure it is valid JSON.", ERROR_CODE));
    }

    public String getErrorCode() {
        return ERROR_CODE;
    }
}
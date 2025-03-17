package ir.ac.ut.ece.ie.miobook.exceptions;

public class InvalidJsonFormatException extends RuntimeException {

    public InvalidJsonFormatException() {
        super("Invalid JSON format. Please check your request.");
    }
}
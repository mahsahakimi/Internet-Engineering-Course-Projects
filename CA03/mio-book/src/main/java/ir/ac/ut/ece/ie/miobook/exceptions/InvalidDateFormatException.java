package ir.ac.ut.ece.ie.miobook.exceptions;

public class InvalidDateFormatException extends RuntimeException {

    public InvalidDateFormatException() {
        super("Please enter the date in the format YYYY-MM-DD (e.g., 2023-12-25).");
    }
}
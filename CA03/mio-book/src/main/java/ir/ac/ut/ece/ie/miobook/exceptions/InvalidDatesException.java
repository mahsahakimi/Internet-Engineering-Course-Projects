package ir.ac.ut.ece.ie.miobook.exceptions;

public class InvalidDatesException extends RuntimeException {

    public InvalidDatesException() {
        super("The dates you entered are not valid.");
    }
}
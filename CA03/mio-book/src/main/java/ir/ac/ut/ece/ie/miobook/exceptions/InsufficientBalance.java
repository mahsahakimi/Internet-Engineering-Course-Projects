package ir.ac.ut.ece.ie.miobook.exceptions;

public class InsufficientBalance extends RuntimeException {
    public InsufficientBalance(String user) {
        super("Payment declined: " + user + "'s current balance is insufficient");
    }
}

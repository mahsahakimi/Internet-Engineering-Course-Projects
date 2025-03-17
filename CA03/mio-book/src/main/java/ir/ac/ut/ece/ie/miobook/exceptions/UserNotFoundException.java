package ir.ac.ut.ece.ie.miobook.exceptions;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String username) {
        super("User " + username + " not found.");
    }
}
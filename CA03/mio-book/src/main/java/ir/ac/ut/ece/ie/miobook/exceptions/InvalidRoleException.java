package ir.ac.ut.ece.ie.miobook.exceptions;

public class InvalidRoleException extends RuntimeException {

    public InvalidRoleException(String role) {
        super("Invalid role: " + role + ". You do not have permission to perform this action.");
    }
}
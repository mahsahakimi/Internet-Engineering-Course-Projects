package ir.ac.ut.ece.ie.miobook.exceptions;

public class PermissionDeniedException extends RuntimeException {

    private static final String ERROR_CODE = "INVALID_ROLE";

    public PermissionDeniedException(String role) {
        super(String.format("Error [%s]: Invalid role '%s'. You do not have permission to perform this action.", ERROR_CODE, role));
    }

    public String getErrorCode() {
        return ERROR_CODE;
    }
}
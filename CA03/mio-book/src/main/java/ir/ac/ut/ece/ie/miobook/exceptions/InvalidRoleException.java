package ir.ac.ut.ece.ie.miobook.exceptions;

public class InvalidRoleException extends RuntimeException {

  private static final String ERROR_CODE = "INVALID_ROLE";

  public InvalidRoleException(String role) {
    super(String.format("Error [%s]: Invalid role: %s. Valid roles are: %s", ERROR_CODE, role, java.util.Arrays.toString(Role.values())));
  }

  public String getErrorCode() {
    return ERROR_CODE;
  }
}
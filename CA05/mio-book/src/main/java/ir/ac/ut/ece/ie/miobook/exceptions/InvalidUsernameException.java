package ir.ac.ut.ece.ie.miobook.exceptions;

public class InvalidUsernameException extends RuntimeException {

  private static final String ERROR_CODE = "INVALID_USERNAME";

  public InvalidUsernameException(String username) {
    super(String.format("Error [%s]: Username is not valid: %s", ERROR_CODE, username));
  }

  public String getErrorCode() {
    return ERROR_CODE;
  }
}
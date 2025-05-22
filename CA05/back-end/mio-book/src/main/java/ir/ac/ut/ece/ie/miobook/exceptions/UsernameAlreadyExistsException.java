package ir.ac.ut.ece.ie.miobook.exceptions;

public class UsernameAlreadyExistsException extends RuntimeException {

  private static final String ERROR_CODE = "USERNAME_DUPLICATE";

  public UsernameAlreadyExistsException(String username) {
    super(String.format("Error [%s]: Username '%s' already exists.", ERROR_CODE, username));
  }

  public String getErrorCode() {
    return ERROR_CODE;
  }
}
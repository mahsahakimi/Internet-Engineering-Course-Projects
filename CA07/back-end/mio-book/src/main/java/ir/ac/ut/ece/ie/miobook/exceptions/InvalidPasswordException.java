package ir.ac.ut.ece.ie.miobook.exceptions;

public class InvalidPasswordException extends RuntimeException {

  private static final String ERROR_CODE = "INVALID_PASSWORD";

  public InvalidPasswordException() {
    super(String.format("Error [%s]: Password is not valid.", ERROR_CODE));
  }

  public String getErrorCode() {
    return ERROR_CODE;
  }
}
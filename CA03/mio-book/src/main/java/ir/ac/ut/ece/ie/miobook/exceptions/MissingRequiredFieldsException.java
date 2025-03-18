package ir.ac.ut.ece.ie.miobook.exceptions;

public class MissingRequiredFieldsException extends RuntimeException {

  private static final String ERROR_CODE = "MISSING_REQUIRED_FIELDS";

  public MissingRequiredFieldsException(String fields) {
    super(String.format("Error [%s]: Please provide all required information for the following fields: %s.", ERROR_CODE, fields));
  }

  public String getErrorCode() {
    return ERROR_CODE;
  }
}
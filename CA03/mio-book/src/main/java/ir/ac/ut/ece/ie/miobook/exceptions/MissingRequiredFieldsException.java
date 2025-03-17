package ir.ac.ut.ece.ie.miobook.exceptions;

public class MissingRequiredFieldsException extends RuntimeException {

  public MissingRequiredFieldsException(String message) {
    super("Please provide all required information: " + message);
  }
}
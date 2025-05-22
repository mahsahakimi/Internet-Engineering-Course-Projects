package ir.ac.ut.ece.ie.miobook.exceptions;

public class BookNotOwnedException extends RuntimeException {

  private static final String ERROR_CODE = "BOOK_NOT_OWNED";

  public BookNotOwnedException() {
    super(String.format("Error [%s]: You must purchase or borrow the book to add a review.", ERROR_CODE));
  }

  public String getErrorCode() {
    return ERROR_CODE;
  }
}

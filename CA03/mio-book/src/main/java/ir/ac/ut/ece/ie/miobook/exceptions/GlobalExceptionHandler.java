package ir.ac.ut.ece.ie.miobook.exceptions;

import ir.ac.ut.ece.ie.miobook.dto.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Response<String>> handleUserNotFoundException(UserNotFoundException ex) {
        Response<String> response = new Response<>("error", ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AuthorNotFoundException.class)
    public ResponseEntity<Response<String>> handleAuthorNotFoundException(AuthorNotFoundException ex) {
        Response<String> response = new Response<>("error", ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PermissionDeniedException.class)
    public ResponseEntity<Response<String>> handlePermissionDeniedException(PermissionDeniedException ex) {
        Response<String> response = new Response<>("error", ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(InvalidDatesException.class)
    public ResponseEntity<Response<String>> handleInvalidDatesException(InvalidDatesException ex) {
        Response<String> response = new Response<>("error", ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingRequiredFieldsException.class)
    public ResponseEntity<Response<String>> handleMissingRequiredFieldsException(MissingRequiredFieldsException ex) {
        Response<String> response = new Response<>("error", ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidDateFormatException.class)
    public ResponseEntity<Response<String>> handleInvalidDateFormatException(InvalidDateFormatException ex) {
        Response<String> response = new Response<>("error", ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidJsonFormatException.class)
    public ResponseEntity<Response<String>> handleInvalidJsonFormatException(InvalidJsonFormatException ex) {
        Response<String> response = new Response<>("error", ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BookTitleAlreadyExistsException.class)
    public ResponseEntity<Response<String>> handleBookTitleAlreadyExistsException(BookTitleAlreadyExistsException ex) {
        Response<String> response = new Response<>("error", ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthorAlreadyExistsException.class)
    public ResponseEntity<Response<String>> handleAuthorAlreadyExistsException(AuthorAlreadyExistsException ex) {
        Response<String> response = new Response<>("error", ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<Response<String>> handleUsernameAlreadyExistsException(UsernameAlreadyExistsException ex) {
        Response<String> response = new Response<>("error", ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<Response<String>> handleEmailAlreadyExistsException(EmailAlreadyExistsException ex) {
        Response<String> response = new Response<>("error", ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MinimumCreditException.class)
    public ResponseEntity<Response<String>> handleMinimumCreditException(MinimumCreditException ex) {
        Response<String> response = new Response<>("error", ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidCreditAmountException.class)
    public ResponseEntity<Response<String>> handleInvalidCreditAmountException(InvalidCreditAmountException ex) {
        Response<String> response = new Response<>("error", ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidAddressFormatException.class)
    public ResponseEntity<Response<String>> handleInvalidAddressFormatException(InvalidAddressFormatException ex) {
        Response<String> response = new Response<>("error", ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidRoleException.class)
    public ResponseEntity<Response<String>> handleInvalidRoleException(InvalidRoleException ex) {
        Response<String> response = new Response<>("error", ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidUsernameException.class)
    public ResponseEntity<Response<String>> handleInvalidUsernameException(InvalidUsernameException ex) {
        Response<String> response = new Response<>("error", ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<Response<String>> handleInvalidPasswordException(InvalidPasswordException ex) {
        Response<String> response = new Response<>("error", ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidEmailException.class)
    public ResponseEntity<Response<String>> handleInvalidEmailException(InvalidEmailException ex) {
        Response<String> response = new Response<>("error", ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidAddressException.class)
    public ResponseEntity<Response<String>> handleInvalidAddressException(InvalidAddressException ex) {
        Response<String> response = new Response<>("error", ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Response<String>> handleRuntimeException(RuntimeException ex) {
        Response<String> response = new Response<>("error", ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BookAlreadyBorrowedException.class)
    public ResponseEntity<Response<String>> handleBookAlreadyBorrowedException(BookAlreadyBorrowedException ex) {
        Response<String> response = new Response<>("error", ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BookAlreadyInCartException.class)
    public ResponseEntity<Response<String>> handleBookAlreadyInCartException(BookAlreadyInCartException ex) {
        Response<String> response = new Response<>("error", ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BookAlreadyOwnedException.class)
    public ResponseEntity<Response<String>> handleBookAlreadyOwnedException(BookAlreadyOwnedException ex) {
        Response<String> response = new Response<>("error", ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<Response<String>> handleBookNotFoundException(BookNotFoundException ex) {
        Response<String> response = new Response<>("error", ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BookNotInCartException.class)
    public ResponseEntity<Response<String>> handleBookNotInCartException(BookNotInCartException ex) {
        Response<String> response = new Response<>("error", ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CartIsEmptyException.class)
    public ResponseEntity<Response<String>> handleCartIsEmptyException(CartIsEmptyException ex) {
        Response<String> response = new Response<>("error", ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CartIsFullException.class)
    public ResponseEntity<Response<String>> handleCartIsFullException(CartIsFullException ex) {
        Response<String> response = new Response<>("error", ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<Response<String>> handleInsufficientBalanceException(InsufficientBalanceException ex) {
        Response<String> response = new Response<>("error", ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.PAYMENT_REQUIRED);
    }

    @ExceptionHandler(AuthorLifespanException.class)
    public ResponseEntity<Response<String>> handleAuthorLifespanException(AuthorLifespanException ex) {
        Response<String> response = new Response<>("error", ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BookAccessDeniedException.class)
    public ResponseEntity<Response<String>> handleBookAccessDeniedException(BookAccessDeniedException ex) {
        Response<String> response = new Response<>("error", ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(BookNotOwnedException.class)
    public ResponseEntity<Response<String>> handleBookNotOwnedException(BookNotOwnedException ex) {
        Response<String> response = new Response<>("error", ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(DaysOutOfRangeException.class)
    public ResponseEntity<Response<String>> handleDaysOutOfRangeException(DaysOutOfRangeException ex) {
        Response<String> response = new Response<>("error", ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidPriceTypeException.class)
    public ResponseEntity<Response<String>> handleInvalidPriceTypeException(InvalidPriceTypeException ex) {
        Response<String> response = new Response<>("error", ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidRatingException.class)
    public ResponseEntity<Response<String>> handleInvalidRatingException(InvalidRatingException ex) {
        Response<String> response = new Response<>("error", ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidYearException.class)
    public ResponseEntity<Response<String>> handleInvalidYearException(InvalidYearException ex) {
        Response<String> response = new Response<>("error", ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoBooksFoundException.class)
    public ResponseEntity<Response<String>> handleNoBooksFoundException(NoBooksFoundException ex) {
        Response<String> response = new Response<>("error", ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoReviewsException.class)
    public ResponseEntity<Response<String>> handleNoReviewsException(NoReviewsException ex) {
        Response<String> response = new Response<>("error", ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidYearRangeException.class)
    public ResponseEntity<Response<String>> handleInvalidYearRange(InvalidYearRangeException ex) {
        Response<String> response = new Response<>("error", ex.getMessage(), null);
        return ResponseEntity.badRequest().body(response);
    }
}
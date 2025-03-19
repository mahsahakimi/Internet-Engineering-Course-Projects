package ir.ac.ut.ece.ie.miobook.exceptions;

import ir.ac.ut.ece.ie.miobook.dto.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Response> handleUserNotFoundException(UserNotFoundException ex) {
        Response response = new Response("error", ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AuthorNotFoundException.class)
    public ResponseEntity<Response> handleAuthorNotFoundException(AuthorNotFoundException ex) {
        Response response = new Response("error", ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<Response> handleBookNotFoundException(BookNotFoundException ex) {
        Response response = new Response("error", ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PermissionDeniedException.class)
    public ResponseEntity<Response> handlePermissionDeniedException(PermissionDeniedException ex) {
        Response response = new Response("error", ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(InvalidDatesException.class)
    public ResponseEntity<Response> handleInvalidDatesException(InvalidDatesException ex) {
        Response response = new Response("error", ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingRequiredFieldsException.class)
    public ResponseEntity<Response> handleMissingRequiredFieldsException(MissingRequiredFieldsException ex) {
        Response response = new Response("error", ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidDateFormatException.class)
    public ResponseEntity<Response> handleInvalidDateFormatException(InvalidDateFormatException ex) {
        Response response = new Response("error", ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidJsonFormatException.class)
    public ResponseEntity<Response> handleInvalidJsonFormatException(InvalidJsonFormatException ex) {
        Response response = new Response("error", ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BookTitleAlreadyExistsException.class)
    public ResponseEntity<Response> handleBookTitleAlreadyExistsException(BookTitleAlreadyExistsException ex) {
        Response response = new Response("error", ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthorAlreadyExistsException.class)
    public ResponseEntity<Response> handleAuthorAlreadyExistsException(AuthorAlreadyExistsException ex) {
        Response response = new Response("error", ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<Response> handleUsernameAlreadyExistsException(UsernameAlreadyExistsException ex) {
        Response response = new Response("error", ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<Response> handleEmailAlreadyExistsException(EmailAlreadyExistsException ex) {
        Response response = new Response("error", ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MinimumCreditException.class)
    public ResponseEntity<Response> handleMinimumCreditException(MinimumCreditException ex) {
        Response response = new Response("error", ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidCreditAmountException.class)
    public ResponseEntity<Response> handleInvalidCreditAmountException(InvalidCreditAmountException ex) {
        Response response = new Response("error", ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidAddressFormatException.class)
    public ResponseEntity<Response> handleInvalidAddressFormatException(InvalidAddressFormatException ex) {
        Response response = new Response("error", ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidRoleException.class)
    public ResponseEntity<Response> handleInvalidRoleException(InvalidRoleException ex) {
        Response response = new Response("error", ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidUsernameException.class)
    public ResponseEntity<Response> handleInvalidUsernameException(InvalidUsernameException ex) {
        Response response = new Response("error", ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<Response> handleInvalidPasswordException(InvalidPasswordException ex) {
        Response response = new Response("error", ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidEmailException.class)
    public ResponseEntity<Response> handleInvalidEmailException(InvalidEmailException ex) {
        Response response = new Response("error", ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidAddressException.class)
    public ResponseEntity<Response> handleInvalidAddressException(InvalidAddressException ex) {
        Response response = new Response("error", ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BookAlreadyBorrowedException.class)
    public ResponseEntity<Response> handleBookAlreadyBorrowedException(BookAlreadyBorrowedException ex) {
        Response response = new Response("error", ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BookAlreadyInCartException.class)
    public ResponseEntity<Response> handleBookAlreadyInCartException(BookAlreadyInCartException ex) {
        Response response = new Response("error", ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BookAlreadyOwnedException.class)
    public ResponseEntity<Response> handleBookAlreadyOwnedException(BookAlreadyOwnedException ex) {
        Response response = new Response("error", ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BookNotInCartException.class)
    public ResponseEntity<Response> handleBookNotInCartException(BookNotInCartException ex) {
        Response response = new Response("error", ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CartIsEmptyException.class)
    public ResponseEntity<Response> handleCartIsEmptyException(CartIsEmptyException ex) {
        Response response = new Response("error", ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CartIsFullException.class)
    public ResponseEntity<Response> handleCartIsFullException(CartIsFullException ex) {
        Response response = new Response("error", ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<Response> handleInsufficientBalanceException(InsufficientBalanceException ex) {
        Response response = new Response("error", ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.PAYMENT_REQUIRED);
    }

    @ExceptionHandler(AuthorLifespanException.class)
    public ResponseEntity<Response> handleAuthorLifespanException(AuthorLifespanException ex) {
        Response response = new Response("error", ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BookAccessDeniedException.class)
    public ResponseEntity<Response> handleBookAccessDeniedException(BookAccessDeniedException ex) {
        Response response = new Response("error", ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(BookNotOwnedException.class)
    public ResponseEntity<Response> handleBookNotOwnedException(BookNotOwnedException ex) {
        Response response = new Response("error", ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(DaysOutOfRangeException.class)
    public ResponseEntity<Response> handleDaysOutOfRangeException(DaysOutOfRangeException ex) {
        Response response = new Response("error", ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidPriceTypeException.class)
    public ResponseEntity<Response> handleInvalidPriceTypeException(InvalidPriceTypeException ex) {
        Response response = new Response("error", ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidRatingException.class)
    public ResponseEntity<Response> handleInvalidRatingException(InvalidRatingException ex) {
        Response response = new Response("error", ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidYearException.class)
    public ResponseEntity<Response> handleInvalidYearException(InvalidYearException ex) {
        Response response = new Response("error", ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SortByException.class)
    public ResponseEntity<Response> handleSortByException(SortByException ex) {
        Response response = new Response("error", ex.getMessage(), null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(SortOrderException.class)
    public ResponseEntity<Response> handleSortOrderException(SortOrderException ex) {
        Response response = new Response("error", ex.getMessage(), null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
    @ExceptionHandler(NoBooksFoundException.class)
    public ResponseEntity<Response> handleNoBooksFoundException(NoBooksFoundException ex) {
        Response response = new Response("error", ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoReviewsException.class)
    public ResponseEntity<Response> handleNoReviewsException(NoReviewsException ex) {
        Response response = new Response("error", ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidYearRangeException.class)
    public ResponseEntity<Response> handleInvalidYearRange(InvalidYearRangeException ex) {
        Response response = new Response("error", ex.getMessage(), null);
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Response> handleRuntimeException(RuntimeException ex) {
        Response response = new Response("error", ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
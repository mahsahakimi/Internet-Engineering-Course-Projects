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
    public ResponseEntity<Response<String>> handleMissingFieldsException(MissingRequiredFieldsException ex) {
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
}
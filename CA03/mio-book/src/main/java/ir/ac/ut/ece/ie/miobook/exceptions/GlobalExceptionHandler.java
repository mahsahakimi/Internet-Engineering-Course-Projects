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

    @ExceptionHandler(InvalidRoleException.class)
    public ResponseEntity<Response<String>> handleInvalidRoleException(InvalidRoleException ex) {
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

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Response<String>> handleRuntimeException(RuntimeException ex) {
        Response<String> response = new Response<>("error", "An unexpected error occurred. Please try again later.", null);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
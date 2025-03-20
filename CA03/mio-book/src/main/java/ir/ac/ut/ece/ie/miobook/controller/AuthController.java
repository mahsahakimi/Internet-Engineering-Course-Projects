package ir.ac.ut.ece.ie.miobook.controller;

import ir.ac.ut.ece.ie.miobook.dto.Response;
import ir.ac.ut.ece.ie.miobook.dto.UserLoginRequest;
import ir.ac.ut.ece.ie.miobook.exceptions.MissingRequestBodyException;
import ir.ac.ut.ece.ie.miobook.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<Response> userLogin(@RequestBody UserLoginRequest request) {
        // Allows a user to log in.
        // Parameters: UserLoginRequest object containing username and password.
        // Returns: Success message if login is successful, or an error if login fails.
        // Example: POST /users/login with UserLoginRequest in the request body.
        if(request == null){
            throw new MissingRequestBodyException();
        }
        request.validate();

        authService.userLogin(request.getUsername(), request.getPassword());
        Response response = new Response("success", "User logged in successfully.", null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<Response> userLogin() {
        // Logs out the currently logged-in user.
        // Returns: Success message after successful logout.
        // Example: POST /users/logout

        authService.userLogout();
        Response response = new Response("success", "User logged out successfully.", null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

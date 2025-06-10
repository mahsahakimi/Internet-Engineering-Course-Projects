package ir.ac.ut.ece.ie.miobook.controller;

import ir.ac.ut.ece.ie.miobook.dto.Response;
import ir.ac.ut.ece.ie.miobook.dto.UserLoginRequest;
import ir.ac.ut.ece.ie.miobook.exceptions.MissingRequestBodyException;
import ir.ac.ut.ece.ie.miobook.exceptions.UserNotLoggedInException;
import ir.ac.ut.ece.ie.miobook.model.User;
import ir.ac.ut.ece.ie.miobook.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

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
        // Example: POST /login with UserLoginRequest in the request body.
        if(request == null){
            throw new MissingRequestBodyException();
        }
        request.validate();

        Map<String, Object> userInfo = authService.loginAndCreateSession(
            request.getUsername(),
            request.getPassword()
        );

        Response response = new Response("success", "User logged in successfully.", userInfo);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<Response> userLogout(HttpServletRequest request) {
        // Logs out the currently logged-in user based on the session ID from the header.
        // Returns: Success message after successful logout.
        // Example: POST /logout with header: Session-ID: <sessionId>

        User user = (User) request.getAttribute("loggedInUser");
        if (user == null) {
            throw new UserNotLoggedInException();
        }
        Response response = new Response("success", "User logged out successfully.", null);
        
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

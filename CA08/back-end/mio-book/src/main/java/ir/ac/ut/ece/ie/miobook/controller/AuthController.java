package ir.ac.ut.ece.ie.miobook.controller;

import ir.ac.ut.ece.ie.miobook.dto.Response;
import ir.ac.ut.ece.ie.miobook.dto.GoogleAuthCodeRequest;
import ir.ac.ut.ece.ie.miobook.dto.UserLoginRequest;
import ir.ac.ut.ece.ie.miobook.exceptions.MissingRequestBodyException;
import ir.ac.ut.ece.ie.miobook.exceptions.UserNotLoggedInException;
import ir.ac.ut.ece.ie.miobook.model.User;
import ir.ac.ut.ece.ie.miobook.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
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

    @PostMapping("/google-code")
    public ResponseEntity<Response> handleGoogleCallback(@RequestBody GoogleAuthCodeRequest request) {
        try {
            System.out.println("\n=>Trying to login via Google account");

            Map<String, Object> userInfo = authService.processGoogleLogin(request.getCode());
            System.out.println("=>Logged in successfully by Google\n");
            
            Response response = new Response("success", "User logged in successfully.", userInfo);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                .body(new Response("error", "Authentication failed", null));
        }
    }


}
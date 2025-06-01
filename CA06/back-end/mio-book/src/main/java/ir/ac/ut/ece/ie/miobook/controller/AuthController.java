package ir.ac.ut.ece.ie.miobook.controller;

import ir.ac.ut.ece.ie.miobook.dto.Response;
import ir.ac.ut.ece.ie.miobook.dto.UserLoginRequest;
import ir.ac.ut.ece.ie.miobook.exceptions.MissingRequestBodyException;
import ir.ac.ut.ece.ie.miobook.model.User;
import ir.ac.ut.ece.ie.miobook.service.AuthService;

import java.util.Map;

import ir.ac.ut.ece.ie.miobook.service.RedisService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final AuthService authService;
    private final RedisService redisService;

    public AuthController(AuthService authService, RedisService redisService) {
        this.authService = authService;
        this.redisService = redisService;
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

        User user = authService.authenticate(
                request.getUsername(),
                request.getPassword()
        );

        String sessionToken = redisService.createSession(user.getUsername());

        Map<String, Object> logged_in = authService.userLogin(request.getUsername(), request.getPassword());
        Response response = new Response("success", "User logged in successfully.", logged_in);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<Response> userLogin() {
        // Logs out the currently logged-in user.
        // Returns: Success message after successful logout.
        // Example: POST /logout

        authService.userLogout();
        Response response = new Response("success", "User logged out successfully.", null);
        
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

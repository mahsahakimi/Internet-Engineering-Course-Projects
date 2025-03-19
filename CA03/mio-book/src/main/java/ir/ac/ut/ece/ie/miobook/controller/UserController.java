package ir.ac.ut.ece.ie.miobook.controller;

import ir.ac.ut.ece.ie.miobook.dto.AddCreditRequest;
import ir.ac.ut.ece.ie.miobook.dto.AddUserRequest;
import ir.ac.ut.ece.ie.miobook.dto.Response;
import ir.ac.ut.ece.ie.miobook.exceptions.MissingRequiredFieldsException;
import ir.ac.ut.ece.ie.miobook.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/add") // (add_user)
    public ResponseEntity<Response> addUser(@RequestBody AddUserRequest request) {
        // Adds a new user to the system.
        // Parameters: role, username, password, email, address.
        // Returns: success message or error if the user already exists or input is invalid.
        // Example: POST /users/add with JSON body containing user details.
        request.validate();
        userService.addUser(request.getRole(), request.getUsername(), request.getPassword(), request.getEmail(), request.getAddress());
        Response response = new Response("success", "User added successfully.", null);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/credit") // (add_credit)
    public ResponseEntity<Response> addCredit(@RequestBody AddCreditRequest request) {
        // Adds credit to a user's account.
        // Parameters: username, credit amount.
        // Returns: success message or error if the user is not found or input is invalid.
        // Example: POST /users/credit with JSON body containing username and credit.
        request.validate();
        userService.addCredit(request.getUsername(), request.getCredit());
        Response response = new Response("success", "Credit added successfully.", null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/details") // (show_user_details)
    public ResponseEntity<Response> showUserDetails(@RequestParam("username") String username) {
        // Retrieves the details of a specific user.
        // Parameters: username.
        // Returns: user details in JSON format or error if the user is not found.
        // Example: GET /users/details?username=user_username.
        if (username == null || username.isEmpty()) {
            throw new MissingRequiredFieldsException("username");
        }

        String userDetails = userService.getUserDetails(username);
        Response response = new Response("success", "User details retrieved.", userDetails);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/purchase_history") // (show_purchase_history)
    public ResponseEntity<Response> showPurchaseHistory(@RequestParam("username") String username) {
        // Retrieves the purchase history of a specific user.
        // Parameters: username.
        // Returns: purchase history in JSON format or error if the user is not found.
        // Example: GET /users/purchase_history?username=user_username.
        if (username == null || username.isEmpty()) {
            throw new MissingRequiredFieldsException("username");
        }

        String purchaseHistory = userService.getPurchaseHistory(username);
        Response response = new Response("success", "Purchase history retrieved.", purchaseHistory);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/purchased_books") // (show_purchased_books)
    public ResponseEntity<Response> showPurchasedBooks(@RequestParam("username") String username) {
        // Retrieves the list of books purchased by a specific user.
        // Parameters: username.
        // Returns: list of purchased books in JSON format or error if the user is not found.
        // Example: GET /users/purchased_books?username=user_username.
        if (username == null || username.isEmpty()) {
            throw new MissingRequiredFieldsException("username");
        }

        String purchasedBooks = userService.getPurchasedBooks(username);
        Response response = new Response("success", "Purchased books retrieved.", purchasedBooks);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
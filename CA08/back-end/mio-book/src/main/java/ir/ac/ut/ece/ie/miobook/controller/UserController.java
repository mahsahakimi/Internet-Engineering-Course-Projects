package ir.ac.ut.ece.ie.miobook.controller;

import ir.ac.ut.ece.ie.miobook.dto.AddCreditRequest;
import ir.ac.ut.ece.ie.miobook.dto.AddUserRequest;
import ir.ac.ut.ece.ie.miobook.dto.Response;
import ir.ac.ut.ece.ie.miobook.exceptions.MissingRequestBodyException;
import ir.ac.ut.ece.ie.miobook.exceptions.UserNotLoggedInException;
import ir.ac.ut.ece.ie.miobook.model.User;
import ir.ac.ut.ece.ie.miobook.service.UserService;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/add") // (add_user)
    public ResponseEntity<Response> addUser(
            @RequestBody AddUserRequest request) {
        // Adds a new user to the system.
        // Parameters: role, username, password, email, address.
        // Returns: success message or error if the user already exists or input is invalid.
        // Example: POST /users/add with JSON body containing user details.
        if(request == null){
            throw new MissingRequestBodyException();
        }

        request.validate();
        userService.addUser(request.getRole(), request.getUsername(), request.getPassword(), request.getEmail(), request.getAddress());
        Response response = new Response("success", "User added successfully.", null);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/addcredit") // (add_credit)
    public ResponseEntity<Response> addCredit(
            HttpServletRequest httpRequest,
            @RequestBody AddCreditRequest request) {
        // Adds credit to a user's account.
        // Parameters: credit amount.
        // Returns: success message or error if the user is not found or input is invalid.
        // Example: POST /users/credit with JSON body containing credit.
        User user = (User) httpRequest.getAttribute("loggedInUser");
        if (user == null) {
            throw new UserNotLoggedInException();
        }

        if(request == null){
            throw new MissingRequestBodyException();
        }

        request.validate();
        int new_balance = userService.addCredit(user, request.getCredit());
        Response response = new Response("success", "Credit added successfully.", new_balance);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/details") // (show_user_details)
    public ResponseEntity<Response> showUserDetails(HttpServletRequest httpRequest) {
        // Retrieves the details of a specific user.
        // Returns: user details in JSON format or error if the user is not found.
        // Example: GET /users/details
        User user = (User) httpRequest.getAttribute("loggedInUser");
        if (user == null) {
            throw new UserNotLoggedInException();
        }

        String userDetails = userService.getUserDetails(user);
        Response response = new Response("success", "User details retrieved.", userDetails);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/purchase_history") // (show_purchase_history)
    public ResponseEntity<Response> showPurchaseHistory(HttpServletRequest httpRequest) {
        // Retrieves the purchase history of user.
        // Returns: purchase history in JSON format or error if the user is not found.
        // Example: GET /users/purchase_history
        User user = (User) httpRequest.getAttribute("loggedInUser");
        if (user == null) {
            throw new UserNotLoggedInException();
        }

        Map<String, Object> purchaseHistory = userService.getPurchaseHistory(user);
        Response response = new Response("success", "Purchase history retrieved.", purchaseHistory);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/purchased_books") // (show_purchased_books)
    public ResponseEntity<Response> showPurchasedBooks(HttpServletRequest httpRequest) {
        // Retrieves the list of books purchased by a specific user.
        // Returns: list of purchased books in JSON format or error if the user is not found.
        // Example: GET /users/purchased_books.
        User user = (User) httpRequest.getAttribute("loggedInUser");
        if (user == null) {
            throw new UserNotLoggedInException();
        }

        Map<String, Object> purchasedBooks = userService.getPurchasedBooks(user);
        Response response = new Response("success", "Purchased books retrieved.", purchasedBooks);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/book_status")
    public ResponseEntity<Response> bookStatus(
        HttpServletRequest httpRequest,
        @RequestParam("title") String title) {
        // in cart, buy, borrow, nothing!
        User user = (User) httpRequest.getAttribute("loggedInUser");
        if (user == null) {
            throw new UserNotLoggedInException();
        }

        String Status = userService.getBookStatus(user, title);
        Response response = new Response("success", "Book status retrieved.", Status);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
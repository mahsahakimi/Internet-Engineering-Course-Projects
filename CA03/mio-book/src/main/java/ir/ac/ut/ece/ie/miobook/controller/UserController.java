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

    @PostMapping("/add") // For adding a new user to the system (add_user)
    public ResponseEntity<Response<String>> addUser(@RequestBody AddUserRequest request) {
        request.validate();
        userService.addUser(request.getRole(), request.getUsername(), request.getPassword(), request.getEmail(), request.getAddress());
        Response<String> response = new Response<>("success", "User added successfully.", null);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/credit") // For adding credit to a user's account (add_credit)
    public ResponseEntity<Response<String>> addCredit(@RequestBody AddCreditRequest request) {
        request.validate();
        userService.addCredit(request.getUsername(), request.getCredit());
        Response<String> response = new Response<>("success", "Credit added successfully.", null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/details") // For getting the details of a user (show_user_details)
    public ResponseEntity<Response<String>> showUserDetails(@RequestParam("username") String username) {
        if (username == null || username.isEmpty()) {
            throw new MissingRequiredFieldsException("username");
        }

        String userDetails = userService.getUserDetails(username);
        Response<String> response = new Response<>("success", "User details retrieved.", userDetails);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/purchase-history") // For getting the purchase history of a user (show_purchase_history)
    public ResponseEntity<Response<String>> showPurchaseHistory(@RequestParam("username") String username) {
        if (username == null || username.isEmpty()) {
            throw new MissingRequiredFieldsException("username");
        }

        String purchaseHistory = userService.getPurchaseHistory(username);
        Response<String> response = new Response<>("success", "Purchase history retrieved.", purchaseHistory);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/purchased-books") // For getting purchased books of a user (show_purchased_books)
    public ResponseEntity<Response<String>> showPurchasedBooks(@RequestParam("username") String username) {
        if (username == null || username.isEmpty()) {
            throw new MissingRequiredFieldsException("username");
        }

        String purchasedBooks = userService.getPurchasedBooks(username);
        Response<String> response = new Response<>("success", "Purchased books retrieved.", purchasedBooks);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
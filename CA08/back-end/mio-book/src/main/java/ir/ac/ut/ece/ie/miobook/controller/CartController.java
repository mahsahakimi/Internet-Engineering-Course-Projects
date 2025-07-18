package ir.ac.ut.ece.ie.miobook.controller;

import ir.ac.ut.ece.ie.miobook.dto.AddToCartRequest;
import ir.ac.ut.ece.ie.miobook.dto.Response;
import ir.ac.ut.ece.ie.miobook.exceptions.MissingRequestBodyException;
import ir.ac.ut.ece.ie.miobook.exceptions.MissingRequiredFieldsException;
import ir.ac.ut.ece.ie.miobook.exceptions.UserNotLoggedInException;
import ir.ac.ut.ece.ie.miobook.model.User;
import ir.ac.ut.ece.ie.miobook.service.CartService;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carts")
public class CartController {

    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/add") // (add_cart)
    public ResponseEntity<Response> addToCart(
            HttpServletRequest httpRequest,
            @RequestBody AddToCartRequest request) {
        // Adds a book to a user's shopping cart.
        // Parameters: title (of the book).
        // Returns: success message or error if the book or user is not found.
        // Example: POST /carts/add with JSON body containing title.
        User user = (User) httpRequest.getAttribute("loggedInUser");
        if (user == null) {
            throw new UserNotLoggedInException();
        }

        if(request == null){
            throw new MissingRequestBodyException();
        }
        request.validate();
        cartService.addToCart(user, request.getTitle(), request.getDays());
        Response response = new Response("success", "Book added to cart successfully.", null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/remove") // (remove_cart)
    public ResponseEntity<Response> removeFromCart(
            HttpServletRequest httpRequest,
            @RequestBody Map<String, String> payload) {
        // Removes a book from the user's shopping cart.
        // Parameters: title (of the book).
        // Returns: success message or error if the book or user is not found.
        // Example: DELETE /carts/remove?title=book_title.
        User user = (User) httpRequest.getAttribute("loggedInUser");
        if (user == null) {
            throw new UserNotLoggedInException();
        }

        String title = payload.get("title");
        if (title == null){
            throw new MissingRequiredFieldsException("username, title.");
        }
        cartService.removeFromCart(user, title);
        Response response = new Response("success", "Book removed from cart successfully.", null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/purchase") // (purchase_cart)
    public ResponseEntity<Response> purchaseCart(HttpServletRequest httpRequest) {
        // Completes the purchase of all books in a user's shopping cart.
        // Returns: success message and purchase details, or error if the cart is empty or user has insufficient credit.
        // Example: POST /carts/purchase with a JSON empty body.
        User user = (User) httpRequest.getAttribute("loggedInUser");
        if (user == null) {
            throw new UserNotLoggedInException();
        }

        cartService.purchaseCart(user);
        Response response = new Response("success", "Purchase completed successfully.", null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/details") // (show_cart)
    public ResponseEntity<Response> showCart(HttpServletRequest httpRequest){
        // Retrieves the contents of the user's shopping cart.
        // Returns: cart details (list of books) in JSON format.
        // Example: GET /carts/details.
        User user = (User) httpRequest.getAttribute("loggedInUser");
        if (user == null) {
            throw new UserNotLoggedInException();
        }

        Map<String, Object> cartDetails = cartService.showCart(user);
        Response response = new Response("success", "Cart details retrieved successfully", cartDetails);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
package ir.ac.ut.ece.ie.miobook.controller;

import ir.ac.ut.ece.ie.miobook.dto.AddToCartRequest;
import ir.ac.ut.ece.ie.miobook.dto.Response;
import ir.ac.ut.ece.ie.miobook.exceptions.MissingRequiredFieldsException;
import ir.ac.ut.ece.ie.miobook.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carts")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/add") // (add_cart)
    public ResponseEntity<Response> addToCart(@RequestBody AddToCartRequest request){
        // Adds a book to a user's shopping cart.
        // Parameters: title (of the book).
        // Returns: success message or error if the book or user is not found.
        // Example: POST /carts/add with JSON body containing title.
        request.validate();

        cartService.addToCart(request.getTitle());
        Response response = new Response("success", "Book added to cart successfully.", null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/remove") // (remove_cart)
    public ResponseEntity<Response> removeFromCart(@RequestParam String title){
        // Removes a book from the user's shopping cart.
        // Parameters: title (of the book).
        // Returns: success message or error if the book or user is not found.
        // Example: DELETE /carts/remove?title=book_title.
        if (title == null){
            throw new MissingRequiredFieldsException("username, title.");
        }

        cartService.removeFromCart(title);
        Response response = new Response("success", "Book removed from cart successfully.", null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/purchase") // (purchase_cart)
    public ResponseEntity<Response> purchaseCart(){
        // Completes the purchase of all books in a user's shopping cart.
        // Returns: success message and purchase details, or error if the cart is empty or user has insufficient credit.
        // Example: POST /carts/purchase with a JSON empty body.

        String purchaseDetails = cartService.purchaseCart();
        Response response = new Response("success", "Purchase completed successfully.", purchaseDetails);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @GetMapping("/details") // (show_cart)
    public ResponseEntity<Response> showCart(){
        // Retrieves the contents of the user's shopping cart.
        // Returns: cart details (list of books) in JSON format.
        // Example: GET /carts/details.

        String cartDetails = cartService.showCart();
        Response response = new Response("success", "Cart details retrieved successfully", cartDetails);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
package ir.ac.ut.ece.ie.miobook.controller;

import ir.ac.ut.ece.ie.miobook.dto.AddToCartRequest;
import ir.ac.ut.ece.ie.miobook.dto.PurchaseCartRequest;
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

    @PostMapping("/add")
    public ResponseEntity<Response<String>> addToCart(@RequestBody AddToCartRequest request){
        request.validate();

        cartService.addToCart(request.getUsername(), request.getTitle());
        Response<String> response = new Response<>("success", "Book added to cart successfully.", null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<Response<String>> removeFromCart(@RequestParam String username, @RequestParam String title){
        if (username == null || title == null){
            throw new MissingRequiredFieldsException("username, title.");
        }

        cartService.removeFromCart(username, title);
        Response<String> response = new Response<>("success", "Book removed from cart successfully.", null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/purchase")
    public ResponseEntity<Response<String>> purchaseCart(@RequestBody PurchaseCartRequest request){
        request.validate();

        String purchaseDetails = cartService.purchaseCart(request.getUsername());
        Response<String> response = new Response<>("success", "Purchase completed successfully.", purchaseDetails);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @GetMapping("/details")
    public ResponseEntity<Response<String>> showCart(@RequestParam String username){
        if (username == null){
            throw new MissingRequiredFieldsException("username.");
        }

        String cartDetails = cartService.showCart(username);
        Response<String> response = new Response<>("success", "Cart details retrieved successfully", cartDetails);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
package ir.ac.ut.ece.ie.miobook.controller;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.ac.ut.ece.ie.miobook.dto.Response;
import ir.ac.ut.ece.ie.miobook.exceptions.InvalidJsonFormatException;
import ir.ac.ut.ece.ie.miobook.exceptions.MissingRequiredFieldsException;
import ir.ac.ut.ece.ie.miobook.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/carts")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<Response<String>> addToCart(@RequestBody String cartJson){
        try{
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(cartJson);

            if (node.get("username") == null || node.get("title") == null){
                throw new MissingRequiredFieldsException("username, title.");
            }

            String username = node.get("username").asText();
            String title = node.get("title").asText();

            cartService.addToCart(username, title);
            Response<String> response = new Response<>("success", "Book added to cart successfully.", null);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (IOException e) {
            throw new InvalidJsonFormatException();
        }
    }

    @PostMapping("/remove")
    public ResponseEntity<Response<String>> removeFromCart(@RequestBody String cartJson){
        try{
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(cartJson);

            if (node.get("username") == null || node.get("title") == null){
                throw new MissingRequiredFieldsException("username, title.");
            }

            String username = node.get("username").asText();
            String title = node.get("title").asText();

            cartService.removeFromCart(username, title);
            Response<String> response = new Response<>("success", "Book removed from cart successfully.", null);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (IOException e) {
            throw new InvalidJsonFormatException();
        }
    }

    @PostMapping("/purchase")
    public ResponseEntity<Response<String>> purchaseCart(@RequestBody String purchaseJson){
        try{
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(purchaseJson);

            if (node.get("username") == null){
                throw new MissingRequiredFieldsException("username.");
            }

            String username = node.get("username").asText();

            String purchaseDetails = cartService.purchaseCart(username);
            Response<String> response = new Response<>("success", "Purchase completed successfully.", purchaseDetails);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (IOException e) {
            throw new InvalidJsonFormatException();
        }
    }
    @GetMapping("/details")
    public ResponseEntity<Response<String>> showCart(@RequestBody String cartJson){
        try{
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(cartJson);

            if (node.get("username") == null){
                throw new MissingRequiredFieldsException("username.");
            }

            String username = node.get("username").asText();

            String cartDetails = cartService.showCart(username);
            Response<String> response = new Response<>("success", "Cart details retrieved successfully", cartDetails);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (IOException e) {
            throw new InvalidJsonFormatException();
        }
    }
}
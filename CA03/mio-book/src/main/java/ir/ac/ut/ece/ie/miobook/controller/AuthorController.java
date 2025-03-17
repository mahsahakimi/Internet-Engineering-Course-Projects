package ir.ac.ut.ece.ie.miobook.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.ac.ut.ece.ie.miobook.dto.Response;
import ir.ac.ut.ece.ie.miobook.exceptions.AuthorNotFoundException;
import ir.ac.ut.ece.ie.miobook.exceptions.InvalidDateFormatException;
import ir.ac.ut.ece.ie.miobook.exceptions.InvalidJsonFormatException;
import ir.ac.ut.ece.ie.miobook.exceptions.MissingRequiredFieldsException;
import ir.ac.ut.ece.ie.miobook.model.MySimpleDate;
import ir.ac.ut.ece.ie.miobook.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.format.DateTimeParseException;

@RestController
@RequestMapping("/authors")
public class AuthorController {

    @Autowired
    private AuthorService authorService;

    @PostMapping("/add") // For adding a new author (add_author)
    public ResponseEntity<Response<String>> addAuthor(@RequestBody String authorJson) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(authorJson);

            if (node.get("username") == null || node.get("name") == null ||
                    node.get("penName") == null || node.get("nationality") == null ||
                    node.get("born") == null) {
                throw new MissingRequiredFieldsException("username, name, penName, nationality, and born.");
            }

            String username = node.get("username").asText();
            String name = node.get("name").asText();
            String penName = node.get("penName").asText();
            String nationality = node.get("nationality").asText();
            String born = node.get("born").asText();
            String died = node.has("died") ? node.get("died").asText() : null;

            try {
                MySimpleDate bornDate = MySimpleDate.parse(born);
                MySimpleDate diedDate = new MySimpleDate();
                if (died != null) {
                    diedDate = MySimpleDate.parse(died);
                }

                authorService.addAuthor(username, name, penName, nationality, bornDate, diedDate);

                Response<String> response = new Response<>("success", "Author added successfully.", null);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } catch (DateTimeParseException e) {
                throw new InvalidDateFormatException();
            }
        } catch (IOException e) {
            throw new InvalidJsonFormatException();
        }
    }

    @GetMapping("/details") // For getting author details (show_author_details)
    public ResponseEntity<Response<String>> getAuthorDetails(@RequestParam("username") String authorName) {
        try {
            String authorDetails = authorService.getAuthorDetails(authorName);
            Response<String> response = new Response<>("success", "Author details retrieved successfully.", authorDetails);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (AuthorNotFoundException e) {
            throw e;
        }
    }
}
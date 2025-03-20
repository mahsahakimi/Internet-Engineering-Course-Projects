package ir.ac.ut.ece.ie.miobook.controller;

import ir.ac.ut.ece.ie.miobook.dto.AddAuthorRequest;
import ir.ac.ut.ece.ie.miobook.dto.Response;
import ir.ac.ut.ece.ie.miobook.exceptions.InvalidDateFormatException;
import ir.ac.ut.ece.ie.miobook.exceptions.MissingRequiredFieldsException;
import ir.ac.ut.ece.ie.miobook.model.MyLocalDate;
import ir.ac.ut.ece.ie.miobook.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeParseException;

@RestController
@RequestMapping("/authors")
public class AuthorController {

    @Autowired
    private AuthorService authorService;

    @PostMapping("/add") // (add_author)
    public ResponseEntity<Response> addAuthor(@RequestBody AddAuthorRequest request) {
        // Adds a new author to the system.
        // Parameters: name, penName, nationality, born (date), died (date, optional).
        // Returns: success message or error if the author already exists or input is invalid.
        // Example: POST /authors/add with JSON body containing author details.
        request.validate();

        MyLocalDate bornDate;
        MyLocalDate diedDate;
        try {
            bornDate = MyLocalDate.parse(request.getBorn());
            diedDate = request.getDied() != null ? MyLocalDate.parse(request.getDied()) : null;
        } catch (DateTimeParseException e) {
            throw new InvalidDateFormatException();
        }

        authorService.addAuthor(request.getName(), request.getPenName(), request.getNationality(), bornDate, diedDate);

        Response response = new Response("success", "Author added successfully.", null);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/details") // (show_author_details)
    public ResponseEntity<Response> getAuthorDetails(@RequestParam("username") String authorName) {
        // Retrieves the details of a specific author.
        // Parameters: username (author's name).
        // Returns: author details in JSON format or error if the author is not found.
        // Example: GET /authors/details?username=author_name.
        if (authorName == null || authorName.isEmpty()) {
            throw new MissingRequiredFieldsException("username");
        }

        String authorDetails = authorService.getAuthorDetails(authorName);
        Response response = new Response("success", "Author details retrieved successfully.", authorDetails);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
package ir.ac.ut.ece.ie.miobook.controller;

import ir.ac.ut.ece.ie.miobook.dto.AddAuthorRequest;
import ir.ac.ut.ece.ie.miobook.dto.Response;
import ir.ac.ut.ece.ie.miobook.exceptions.InvalidDateFormatException;
import ir.ac.ut.ece.ie.miobook.exceptions.MissingRequestBodyException;
import ir.ac.ut.ece.ie.miobook.exceptions.MissingRequiredFieldsException;
import ir.ac.ut.ece.ie.miobook.exceptions.UserNotLoggedInException;
import ir.ac.ut.ece.ie.miobook.model.MyLocalDate;
import ir.ac.ut.ece.ie.miobook.model.User;
import ir.ac.ut.ece.ie.miobook.service.AuthorService;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/authors")
public class AuthorController {

    private final AuthorService authorService;

    @Autowired
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @PostMapping("/add") // (add_author)
    public ResponseEntity<Response> addAuthor(
        HttpServletRequest httpRequest,
        @RequestBody AddAuthorRequest request) {
        // Adds a new author to the system.
        // Parameters: name, penName, nationality, born (date), died (date, optional).
        // Returns: success message or error if the author already exists or input is invalid.
        // Example: POST /authors/add with JSON body containing author details.
        User user = (User) httpRequest.getAttribute("loggedInUser");
        if (user == null) {
            throw new UserNotLoggedInException();
        }

        if(request == null){
            throw new MissingRequestBodyException();
        }

        request.validate();
        MyLocalDate bornDate;
        MyLocalDate diedDate;
        try {
            bornDate = MyLocalDate.parse(request.getBorn());
            diedDate = request.getDied() != "" ? MyLocalDate.parse(request.getDied()) : null;
        } catch (DateTimeParseException e) {
            throw new InvalidDateFormatException();
        }

         List<Map<String, Object>> newList = authorService.addAuthor( user, request.getName(), request.getPenName(), request.getNationality(), bornDate, diedDate);

        Response response = new Response("success", "Author added successfully.", newList);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/details") // (show_author_details)
    public ResponseEntity<Response> getAuthorDetails(
            HttpServletRequest request,
            @RequestParam("username") String authorName) {
        // Retrieves the details of a specific author.
        // Parameters: username (author's name).
        // Returns: author details in JSON format or error if the author is not found.
        // Example: GET /authors/details?username=author_name.
        User user = (User) request.getAttribute("loggedInUser");
        if (user == null) {
            throw new UserNotLoggedInException();
        }
        
        if (authorName == null || authorName.isEmpty()) {
            throw new MissingRequiredFieldsException("username");
        }

        Map<String,Object> authorDetails = authorService.getAuthorDetails(authorName);
        Response response = new Response("success", "Author details retrieved successfully.", authorDetails);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/getAll") // (show_author_details)
    public ResponseEntity<Response> getAuthors(HttpServletRequest request) {
        User user = (User) request.getAttribute("loggedInUser");
        if (user == null) {
            throw new UserNotLoggedInException();
        }


        List<Map<String, Object>> authors = authorService.getAllAuthors(1,10);
        Response response = new Response("success", "Authors retrieved successfully.", authors);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    
}
package ir.ac.ut.ece.ie.miobook.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;

import ir.ac.ut.ece.ie.miobook.dto.Response;
import ir.ac.ut.ece.ie.miobook.exceptions.UserNotLoggedInException;
import ir.ac.ut.ece.ie.miobook.model.User;
import ir.ac.ut.ece.ie.miobook.service.GenreService;


@RestController
@RequestMapping("/genres")
public class GenreController {
    
    @Autowired
    private GenreService genreService;

    @Autowired
    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }


    @GetMapping("/getAll")
    public ResponseEntity<Response> getAllGenres(HttpServletRequest httpRequest) {
        User user = (User) httpRequest.getAttribute("loggedInUser");
        if (user == null) {
            throw new UserNotLoggedInException();
        }

        List<String> genres = genreService.getAllGenres();
        Response response = new Response("success", "Purchased books retrieved.", genres);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

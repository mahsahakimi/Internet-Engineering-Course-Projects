package ir.ac.ut.ece.ie.miobook.controller;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ir.ac.ut.ece.ie.miobook.dto.Response;
import ir.ac.ut.ece.ie.miobook.service.GenreService;


@RestController
@RequestMapping("/genres")
public class GenreController {
    
    @Autowired
    private GenreService genreService;

    @GetMapping("/getAll")
    public ResponseEntity<Response> getAllGenres() {
        List<String> genres = genreService.getAllGenres();
        Response response = new Response("success", "Purchased books retrieved.", genres);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

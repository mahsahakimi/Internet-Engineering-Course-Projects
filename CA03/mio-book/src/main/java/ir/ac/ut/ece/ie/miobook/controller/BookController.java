package ir.ac.ut.ece.ie.miobook.controller;

import ir.ac.ut.ece.ie.miobook.dto.AddBookRequest;
import ir.ac.ut.ece.ie.miobook.dto.AddReviewRequest;
import ir.ac.ut.ece.ie.miobook.dto.BorrowBookRequest;
import ir.ac.ut.ece.ie.miobook.dto.Response;
import ir.ac.ut.ece.ie.miobook.exceptions.*;
import ir.ac.ut.ece.ie.miobook.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @PostMapping("/add") // (add_book)
    public ResponseEntity<Response<String>> addBook(@RequestBody AddBookRequest request) {
        request.validate();

        String priceString = request.getPrice(); // Assuming it's a String
        Object price = null;
        if (priceString.contains(".")) {
            price = Double.parseDouble(priceString);
        } else {
            price = Integer.parseInt(priceString);
        }
        int finalPrice;
        if (price instanceof Double) {
            finalPrice = (int) Math.round((Double) price * 100); // converting to cents
        } else {
            finalPrice = (Integer) price;
        }

        bookService.addBook(request.getUsername(), request.getTitle(), request.getAuthor(),
                request.getPublisher(), request.getYear(), finalPrice, request.getGenres(),
                request.getSynopsis(), request.getContent());

        Response<String> response = new Response<>("success", "Book added successfully.", null);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/borrow") // (borrow_book)
    public ResponseEntity<Response<String>> borrowBook(@RequestBody BorrowBookRequest request) {
        request.validate();
        bookService.borrowBook(request.getUsername(), request.getTitle(), request.getDays());
        Response<String> response = new Response<>("success", "Book added successfully.", null);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/add_review") // (add_review)
    public ResponseEntity<Response<String>> add_review(@RequestBody AddReviewRequest request) {
        request.validate();
        bookService.addReview(request.getUsername(), request.getTitle(), request.getRate(), request.getComment());
        Response<String> response = new Response<>("success", "Review added successfully", null);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/details") // (show_book_details)
    public ResponseEntity<Response<String>> showBookDetails(@RequestParam("title") String title) {
        if (title == null || title.isEmpty()) {
            throw new MissingRequiredFieldsException("title");
        }

        String bookDetails = bookService.getBookDetails(title);
        Response<String> response = new Response<>("success", "Book details retrieved.", bookDetails);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/content") // (show_book_content)
    public ResponseEntity<Response<String>> showBookContent(@RequestParam("title") String title, @RequestParam("username") String username) {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Missing 'username' field.");
        }
        if (title == null || title.isEmpty()) {
            throw new MissingRequiredFieldsException("title");
        }

        String bookContent = bookService.showBookContent(username, title);
        Response<String> response = new Response<>("success", "Book content retrieved.", bookContent);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/reviews") // (show_book_reviews)
    public ResponseEntity<Response<String>> showBookReviews(@RequestParam("title") String title,
                                                            @RequestParam(value = "page", defaultValue = "1") int page,
                                                            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        if (title == null || title.isEmpty()) {
            throw new MissingRequiredFieldsException("title");
        }

        String bookReviews = bookService.getBookReviews(title, page, pageSize);
        Response<String> response = new Response<>("success", "Book reviews retrieved.", bookReviews);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/search_by_title") // (search_books_by_title)
    public ResponseEntity<Response<String>> searchByTitle(@RequestParam(value = "title") String title,
                                                            @RequestParam(value = "page", defaultValue = "1") int page,
                                                            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        if (title == null || title.isEmpty()) {
            throw new MissingRequiredFieldsException("title");
        }
        String responseJson = bookService.searchByTitle(title, page, pageSize);
        Response<String> response = new Response<>("success", "Books found successfully", responseJson);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/search_by_author") // (search_books_by_author)
    public ResponseEntity<Response<String>> searchByAuthor(@RequestParam(value = "name") String authorName,
                                                            @RequestParam(value = "page", defaultValue = "1") int page,
                                                            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        if (authorName == null || authorName.isEmpty()){
            throw new MissingRequiredFieldsException("author");
        }

        String responseJson = bookService.searchByAuthor(authorName, page, pageSize);
        Response<String> response = new Response<>("success", "Books found successfully", responseJson);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/search_by_genre") // (search_books_by_genre)
    public ResponseEntity<Response<String>> searchByGenre(@RequestParam(value = "genre") String genre,
                                                            @RequestParam(value = "page", defaultValue = "1") int page,
                                                            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

        if (genre == null || genre.isEmpty()) {
            throw new MissingRequiredFieldsException("genre");
        }

        String responseJson = bookService.searchByGenre(genre, page, pageSize);
        Response<String> response = new Response<>("success", "Books found successfully", responseJson);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/search_by_year") // (search_books_by_year)
    public ResponseEntity<Response<String>> searchByYear(@RequestParam(value = "from") Integer fromYear,
                                                            @RequestParam(value = "to") Integer toYear,
                                                            @RequestParam(value = "page", defaultValue = "1") int page,
                                                            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

        if (fromYear == null) {
            throw new MissingRequiredFieldsException("from year.");
        }
        if (toYear == null) {
            throw new MissingRequiredFieldsException("to year.");
        }

        String responseJson = bookService.searchByYear(fromYear, toYear, page, pageSize);
        Response<String> response = new Response<>("success", "Books found successfully", responseJson);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/searchBooks") // (search_books)
    public ResponseEntity<Response<String>> searchBooks(@RequestParam(value = "title", required = false) String title,
                                                        @RequestParam(value = "author", required = false) String authorName,
                                                        @RequestParam(value = "genre", required = false) String genre,
                                                        @RequestParam(value = "fromYear", required = false) Integer fromYear,
                                                        @RequestParam(value = "toYear", required = false) Integer toYear,
                                                        @RequestParam(value = "sortBy", required = false) String sortBy,
                                                        @RequestParam(value = "sortOrder", required = false) String sortOrder,
                                                        @RequestParam(value = "page", defaultValue = "1") int page,
                                                        @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

        if (fromYear == null) {
            throw new MissingRequiredFieldsException("from year.");
        }
        if (toYear == null) {
            throw new MissingRequiredFieldsException("to year.");
        }

        // Check for valid sortBy and sortOrder
        List<String> validSortBy = Arrays.asList("averageRating", "reviewCount", null);
        List<String> validSortOrder = Arrays.asList("asc", "desc", null);

        if (sortBy != null && !validSortBy.contains(sortBy)) {
            throw new SortByException(sortBy);
        }

        if (sortOrder != null && !validSortOrder.contains(sortOrder)) {
            throw new SortOrderException(sortOrder);
        }

        String responseJson = bookService.searchBooks(title, authorName, genre, fromYear, toYear, sortBy, sortOrder, page, pageSize);
        Response<String> response = new Response<>("success", "Books retrieved successfully.", responseJson);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

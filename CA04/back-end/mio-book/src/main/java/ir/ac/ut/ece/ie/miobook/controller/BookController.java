package ir.ac.ut.ece.ie.miobook.controller;

import ir.ac.ut.ece.ie.miobook.dto.AddBookRequest;
import ir.ac.ut.ece.ie.miobook.dto.AddReviewRequest;
import ir.ac.ut.ece.ie.miobook.dto.BorrowBookRequest;
import ir.ac.ut.ece.ie.miobook.dto.Response;
import ir.ac.ut.ece.ie.miobook.exceptions.*;
import ir.ac.ut.ece.ie.miobook.model.Book;
import ir.ac.ut.ece.ie.miobook.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @PostMapping("/add") // (add_book)
    public ResponseEntity<Response> addBook(@RequestBody AddBookRequest request) {
        // Adds a new book to the library. Requires admin privileges.
        // Parameters: title, author, publisher, year, price, genres, synopsis, content.
        // Returns: success message or error if book already exists or input is invalid.
        // Example: POST /books/add with JSON body containing book details.
        if(request == null){
            throw new MissingRequestBodyException();
        }
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

        bookService.addBook(request.getTitle(), request.getAuthor(),
                request.getPublisher(), request.getYear(), finalPrice, request.getGenres(),
                request.getSynopsis(), request.getContent());

        Response response = new Response("success", "Book added successfully.", null);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/borrow") // (borrow_book)
    public ResponseEntity<Response> borrowBook(@RequestBody BorrowBookRequest request) {
        // Allows a user to borrow a book.
        // Parameters: title, days (number of days to borrow).
        // Returns: success message or error if book is not available or user has insufficient credit.
        // Example: POST /books/borrow with JSON body containing username, title, and days.
        if(request == null){
            throw new MissingRequestBodyException();
        }
        request.validate();

        bookService.borrowBook(request.getTitle(), request.getDays());
        Response response = new Response("success", "Book added successfully.", null);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/add_review") // (add_review)
    public ResponseEntity<Response> add_review(@RequestBody AddReviewRequest request) {
        // Adds a review for a book.
        // Parameters: title, rate (1-5), comment.
        // Returns: success message or error if book or user not found or rate is invalid.
        // Example: POST /books/add_review with JSON body containing review details.
        if(request == null){
            throw new MissingRequestBodyException();
        }
        request.validate();

        bookService.addReview(request.getTitle(), request.getRate(), request.getComment());
        Response response = new Response("success", "Review added successfully", null);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/details") // (show_book_details)
    public ResponseEntity<Response> showBookDetails(@RequestParam("title") String title) {
        // Retrieves details of a specific book.
        // Parameters: title (book title).
        // Returns: book details in JSON format or error if book not found.
        // Example: GET /books/details?title=ExampleBookTitle.
        if (title == null || title.isEmpty()) {
            throw new MissingRequiredFieldsException("title");
        }

        String bookDetails = bookService.getBookDetails(title);
        Response response = new Response("success", "Book details retrieved.", bookDetails);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/content") // (show_book_content)
    public ResponseEntity<Response> showBookContent(@RequestParam("title") String title) {
        // Retrieves the content of a book for a specific user (if they have borrowed it).
        // Parameters: title (book title).
        // Returns: book content in JSON format or error if book not found or user hasn't borrowed it.
        // Example: GET /books/content?title=ExampleBookTitle.
        if (title == null || title.isEmpty()) {
            throw new MissingRequiredFieldsException("title");
        }

        String bookContent = bookService.showBookContent(title);
        Response response = new Response("success", "Book content retrieved.", bookContent);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/reviews") // (show_book_reviews)
    public ResponseEntity<Response> showBookReviews(@RequestParam("title") String title,
                                                            @RequestParam(value = "page", defaultValue = "1") int page,
                                                            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        // Retrieves reviews for a specific book with pagination.
        // Parameters: title (book title), page (optional, default 1), pageSize (optional, default 10).
        // Returns: reviews in JSON format or error if book not found.
        // Example: GET /books/reviews?title=ExampleBookTitle&page=2&pageSize=20.
        if (title == null || title.isEmpty()) {
            throw new MissingRequiredFieldsException("title");
        }

        String bookReviews = bookService.getBookReviews(title, page, pageSize);
        Response response = new Response("success", "Book reviews retrieved.", bookReviews);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/search_by_title") // (search_books_by_title)
    public ResponseEntity<Response> searchByTitle(@RequestParam(value = "title") String title,
                                                            @RequestParam(value = "page", defaultValue = "1") int page,
                                                            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        // Searches books by title with pagination.
        // Parameters: title (search term), page (optional, default 1), pageSize (optional, default 10).
        // Returns: list of books matching the title in JSON format.
        // Example: GET /books/search_by_title?title=ExampleTitle&page=1&pageSize=10.
        if (title == null || title.isEmpty()) {
            throw new MissingRequiredFieldsException("title");
        }

        String responseJson = bookService.searchByTitle(title, page, pageSize);
        Response response = new Response("success", "Books found successfully", responseJson);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/search_by_author") // (search_books_by_author)
    public ResponseEntity<Response> searchByAuthor(@RequestParam(value = "name") String authorName,
                                                            @RequestParam(value = "page", defaultValue = "1") int page,
                                                            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        // Searches books by author name with pagination.
        // Parameters: name (author name), page (optional, default 1), pageSize (optional, default 10).
        // Returns: list of books by the author in JSON format.
        // Example: GET /books/search_by_author?name=ExampleAuthor&page=1&pageSize=10.
        if (authorName == null || authorName.isEmpty()){
            throw new MissingRequiredFieldsException("author");
        }

        String responseJson = bookService.searchByAuthor(authorName, page, pageSize);
        Response response = new Response("success", "Books found successfully", responseJson);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/search_by_genre") // (search_books_by_genre)
    public ResponseEntity<Response> searchByGenre(@RequestParam(value = "genre") String genre,
                                                            @RequestParam(value = "page", defaultValue = "1") int page,
                                                            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        // Searches books by genre with pagination.
        // Parameters: genre (book genre), page (optional, default 1), pageSize (optional, default 10).
        // Returns: list of books matching the genre in JSON format.
        // Example: GET /books/search_by_genre?genre=Fantasy&page=1&pageSize=10.
        if (genre == null || genre.isEmpty()) {
            throw new MissingRequiredFieldsException("genre");
        }

        String responseJson = bookService.searchByGenre(genre, page, pageSize);
        Response response = new Response("success", "Books found successfully", responseJson);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/search_by_year") // (search_books_by_year)
    public ResponseEntity<Response> searchByYear(@RequestParam(value = "from") Integer fromYear,
                                                            @RequestParam(value = "to") Integer toYear,
                                                            @RequestParam(value = "page", defaultValue = "1") int page,
                                                            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        // Searches books published within a year range with pagination.
        // Parameters: from (start year), to (end year), page (optional, default 1), pageSize (optional, default 10).
        // Returns: list of books published within the year range in JSON format.
        // Example: GET /books/search_by_year?from=2020&to=2023&page=1&pageSize=10.
        if (fromYear == null) {
            throw new MissingRequiredFieldsException("from year.");
        }
        if (toYear == null) {
            throw new MissingRequiredFieldsException("to year.");
        }

        String responseJson = bookService.searchByYear(fromYear, toYear, page, pageSize);
        Response response = new Response("success", "Books found successfully", responseJson);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/searchBooks") // (search_books)
    public ResponseEntity<Response> searchBooks(@RequestParam(value = "title", required = false) String title,
                                                        @RequestParam(value = "author", required = false) String authorName,
                                                        @RequestParam(value = "genre", required = false) String genre,
                                                        @RequestParam(value = "fromYear", required = false) Integer fromYear,
                                                        @RequestParam(value = "toYear", required = false) Integer toYear,
                                                        @RequestParam(value = "sortBy", required = false) String sortBy,
                                                        @RequestParam(value = "sortOrder", required = false) String sortOrder,
                                                        @RequestParam(value = "page", defaultValue = "1") int page,
                                                        @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        // Advanced search for books with multiple filters and sorting options with pagination.
        // Parameters: title (optional), author (optional), genre (optional), fromYear (optional), toYear (optional),
        //             sortBy (optional: averageRating, reviewCount), sortOrder (optional: asc, desc),
        //             page (optional, default 1), pageSize (optional, default 10).
        // Returns: list of books matching the search criteria in JSON format.
        // Example: GET /books/searchBooks?title=ExampleTitle&author=ExampleAuthor&genre=Fantasy&fromYear=2020&toYear=2023&sortBy=averageRating&sortOrder=desc&page=1&pageSize=10.
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
        Response response = new Response("success", "Books retrieved successfully.", responseJson);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @GetMapping("/newRelease")
    public ResponseEntity<Response> newRelease() {
        List<Map<String, Object>> newestBooks = bookService.newRelease();
        Response response = new Response("success", "Books retrieved successfully.", newestBooks);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/topRated")
    public ResponseEntity<Response> topRated() {
        List<Map<String, Object>> topBooks = bookService.topRated(1, 5);
        Response response = new Response("success", "Books retrieved successfully.", topBooks);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}

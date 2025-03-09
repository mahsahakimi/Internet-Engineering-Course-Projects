package ir.ac.ut.ece.ie.miobook.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import ir.ac.ut.ece.ie.miobook.database.BookDatabase;
import ir.ac.ut.ece.ie.miobook.database.ReviewDatabase;
import ir.ac.ut.ece.ie.miobook.model.Book;
import ir.ac.ut.ece.ie.miobook.model.Review;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShowBookDetailsService {

    public static String getBookDetails(String title) throws IOException {
        Book book = BookDatabase.getInstance().findBookByTitle(title);

        if (book == null) {
            throw new IllegalArgumentException("Book not found.");
        }

        Map<String, Object> bookData = new HashMap<>();
        bookData.put("title", book.getTitle());
        bookData.put("author", book.getAuthor());
        bookData.put("publisher", book.getPublisher());
        bookData.put("genres", book.getGenres());
        bookData.put("year", book.getYear());
        bookData.put("price", book.getPrice());
        bookData.put("synopsis", book.getSynopsis());

        // Add average rating from reviews
        List<Review> reviews = ReviewDatabase.getInstance().getReviewsByBook(book);
        if (reviews != null && !reviews.isEmpty()) {
            double totalRating = 0;
            for (Review review : reviews) {
                totalRating += review.getRate();
            }
            double averageRating = totalRating / reviews.size();
            bookData.put("averageRating", averageRating);
        } else {
            bookData.put("averageRating", "No ratings yet");
        }

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Book details retrieved successfully.");
        response.put("data", bookData);

        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(response);
    }
}

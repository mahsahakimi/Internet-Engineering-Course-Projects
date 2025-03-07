package ir.ac.ut.ece.ie.miobook.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import ir.ac.ut.ece.ie.miobook.database.BookDatabase;
import ir.ac.ut.ece.ie.miobook.database.ReviewDatabase;
import ir.ac.ut.ece.ie.miobook.model.Book;
import ir.ac.ut.ece.ie.miobook.model.Review;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShowBookReviewsService {

    public static String getBookReviews(String title) throws IOException {
        Book book = BookDatabase.getInstance().findBookByTitle(title);
        if (book == null) {
            throw new IllegalArgumentException("Book does not exist!");
        }

        int count = 0;
        int totalRate = 0;
        List<Review> reviews = ReviewDatabase.getInstance().getReviews();

        List<Map<String, Object>> outputReviews = new ArrayList<>();
        for (Review r : reviews) {
            if (r.getTitle().equals(title)) {
                count += 1;
                totalRate += r.getRate();
                Map<String, Object> newResponse = new HashMap<>();
                newResponse.put("username", r.getUsername());
                newResponse.put("rate", r.getRate());
                newResponse.put("comment", r.getComment());
                outputReviews.add(newResponse);
            }
        }

        if (count == 0) {
            throw new IllegalArgumentException("There is no review for this book.");
        } else {
            Map<String, Object> data = new HashMap<>();
            data.put("title", title);
            data.put("reviews", outputReviews);
            data.put("averageRating", totalRate / count);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Book reviews retrieved successfully.");
            response.put("data", data);

            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(response);
        }
    }
}

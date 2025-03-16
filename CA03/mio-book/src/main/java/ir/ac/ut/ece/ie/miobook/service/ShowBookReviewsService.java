package ir.ac.ut.ece.ie.miobook.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import ir.ac.ut.ece.ie.miobook.database.BookDatabase;
import ir.ac.ut.ece.ie.miobook.database.ReviewDatabase;
import ir.ac.ut.ece.ie.miobook.model.Book;
import ir.ac.ut.ece.ie.miobook.model.Review;
import ir.ac.ut.ece.ie.miobook.utils.PaginationUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ShowBookReviewsService {

    public static String getBookReviews(String title, int page, int pageSize) throws IOException {
        Book book = BookDatabase.getInstance().findBookByTitle(title);
        if (book == null) {
            throw new IllegalArgumentException("Book does not exist!");
        }

        List<Review> allReviews = ReviewDatabase.getInstance().getReviews();
        if (allReviews.isEmpty()) {
            throw new IllegalArgumentException("There is no review for this book.");
        } else {
            List<Review> paginatedReviews = PaginationUtils.paginate(allReviews, page, pageSize);
            List<Map<String, Object>> outputReviews = paginatedReviews.stream()
                    .filter(r -> r.getTitle().equals(title))
                    .map(r -> {
                        Map<String, Object> newResponse = new HashMap<>();
                        newResponse.put("username", r.getUsername());
                        newResponse.put("rate", r.getRate());
                        newResponse.put("comment", r.getComment());
                        return newResponse;
                    })
                    .collect(Collectors.toList());

            Map<String, Object> data = new HashMap<>();
            data.put("title", title);
            data.put("reviews", outputReviews);
            data.put("averageRating", ReviewDatabase.getInstance().calculateAverageRating(book));

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Book reviews retrieved successfully.");
            // TODO: add page and page size if needed!
            response.put("data", data);

            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(response);
        }
    }
}

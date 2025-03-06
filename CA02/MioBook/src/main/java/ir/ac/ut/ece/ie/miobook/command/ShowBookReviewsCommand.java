package ir.ac.ut.ece.ie.miobook.command;

import com.fasterxml.jackson.databind.JsonNode;
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

public class ShowBookReviewsCommand {
    public static void execute(String inputJson) throws IOException {
        if (inputJson == null) {
            System.out.println("Input data for show_book_details command is missing.");
            return;
        }

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(inputJson);

            String title = node.get("title").asText();
            Book book = BookDatabase.getInstance().findBookByTitle(title);
            if(book == null){
                throw new Exception("Book does not exist!");
            }

            int count = 0;
            int totalRate = 0;
            List<Review> reviews = ReviewDatabase.getInstance().getReviews();

            List <Map<String, Object>> outputReviews = new ArrayList<>();
            for(Review r : reviews){
                if(r.getTitle().equals(title)){
                    count += 1;
                    totalRate += r.getRate();
                    Map<String, Object> newResponse = new HashMap<>();
                    newResponse.put("username", r.getUsername());
                    newResponse.put("rate", r.getRate());
                    newResponse.put("comment", r.getComment());
                    outputReviews.add(newResponse);
                }

            }

            if(count == 0){
                throw new Exception("there is no review for this book");
            }
            else{
                Map<String, Object> data = new HashMap<>();
                data.put("title", title);
                data.put("reviews", outputReviews);
                data.put("averageRating", totalRate/count);

                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Book reviews retrieved successfully");
                response.put("data", data);
                System.out.println(mapper.writeValueAsString(response));
            }

        }catch (Exception e) {
            System.out.println("{\"success\": false, \"message\": \"" + e.getMessage() + "\"}");
        }
    }
}
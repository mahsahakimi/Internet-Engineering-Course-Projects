package ir.ac.ut.ece.ie.miobook.command;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.ac.ut.ece.ie.miobook.service.ShowBookReviewsService;
import java.io.IOException;

public class ShowBookReviewsCommand {

    public static void execute(String inputJson) throws IOException {
        if (inputJson == null) {
            throw new IllegalArgumentException("Input data for show_book_reviews command is missing.");
        }

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(inputJson);

        // Check required fields
        if (node.get("title") == null) {
            throw new IllegalArgumentException("Missing required fields.");
        }

        String title = node.get("title").asText();

        // Call the service to do the business logic
        String responseJson = ShowBookReviewsService.getBookReviews(title);

        System.out.println(responseJson);
    }
}
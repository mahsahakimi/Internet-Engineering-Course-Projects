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

        int page = node.has("page") && node.get("page").isInt() ? node.get("page").asInt() : 1;
        int pageSize = node.has("pageSize") && node.get("pageSize").isInt() ? node.get("pageSize").asInt() : 10;

        // Call the service to do the business logic
        String responseJson = ShowBookReviewsService.getBookReviews(title, page, pageSize);

        System.out.println(responseJson);
    }
}
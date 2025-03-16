package ir.ac.ut.ece.ie.miobook.command;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.ac.ut.ece.ie.miobook.service.AddReviewService;

import java.io.IOException;

public class AddReviewCommand {

    public static void execute(String inputJson) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(inputJson);

        if (node.get("username") == null) {
            throw new IllegalArgumentException("Missing 'username' field.");
        }
        String username = node.get("username").asText();

        if (node.get("title") == null) {
            throw new IllegalArgumentException("Missing 'title' field.");
        }
        String title = node.get("title").asText();

        if (node.get("rate") == null) {
            throw new IllegalArgumentException("Missing 'rate' field.");
        }
        int rate = node.get("rate").asInt();

        if (node.get("comment") == null) {
            throw new IllegalArgumentException("Missing 'comment' field.");
        }
        String comment = node.get("comment").asText();

        AddReviewService.addReview(username, title, rate, comment);
        System.out.println("{\"success\": true, \"message\": \"Review added successfully.\"}");
    }
}
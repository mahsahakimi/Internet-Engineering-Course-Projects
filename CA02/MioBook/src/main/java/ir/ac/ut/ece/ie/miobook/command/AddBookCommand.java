package ir.ac.ut.ece.ie.miobook.command;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.ac.ut.ece.ie.miobook.service.AddBookService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AddBookCommand {

    public static void execute(String inputJson) throws IOException {
        if (inputJson == null) {
            throw new IllegalArgumentException("Input data for add_book command is missing.");
        }

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(inputJson);

        // Check required fields
        if (node.get("username") == null || node.get("title") == null ||
                node.get("author") == null || node.get("publisher") == null ||
                node.get("year") == null || node.get("price") == null ||
                node.get("synopsis") == null || node.get("content") == null ||
                node.get("genres") == null) {
            throw new IllegalArgumentException("Missing required fields.");
        }

        String username = node.get("username").asText();
        String title = node.get("title").asText();
        String authorName = node.get("author").asText();
        String publisher = node.get("publisher").asText();
        int year = node.get("year").asInt();
        Object price = node.get("price");
        String synopsis = node.get("synopsis").asText();
        String content = node.get("content").asText();

        JsonNode genresNode = node.get("genres");
        List<String> genres = new ArrayList<>();
        for (JsonNode genreNode : genresNode) {
            genres.add(genreNode.asText());
        }

        // Call the service to do the business logic
        AddBookService.addBook(username, title, authorName, publisher, year, price, genres, synopsis, content);

        System.out.println("{\"success\": true, \"message\": \"Book added successfully.\"}");
    }
}

package ir.ac.ut.ece.ie.miobook.command;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.ac.ut.ece.ie.miobook.model.MySimpleDate;
import ir.ac.ut.ece.ie.miobook.service.AddAuthorService;

import java.io.IOException;
import java.time.format.DateTimeParseException;

public class AddAuthorCommand {

    public static void execute(String inputJson) throws IOException {
        if (inputJson == null) {
            throw new IllegalArgumentException("Input data for add_author command is missing.");
        }

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(inputJson);

        // Check required fields
        if (node.get("username") == null || node.get("name") == null ||
                node.get("penName") == null || node.get("nationality") == null ||
                node.get("born") == null) {
            throw new IllegalArgumentException("Missing required fields.");
        }

        String username = node.get("username").asText();
        String name = node.get("name").asText();
        String penName = node.get("penName").asText();
        String nationality = node.get("nationality").asText();
        String born = node.get("born").asText();
        String died = node.has("died") ? node.get("died").asText() : null;

        try {
            MySimpleDate bornDate = MySimpleDate.parse(born);
            MySimpleDate diedDate = new MySimpleDate();
            if (died != null) {
                diedDate = MySimpleDate.parse(died);
            }

            // Call the service to do the business logic
            AddAuthorService.addAuthor(username, name, penName, nationality, bornDate, diedDate);

            System.out.println("{\"success\": true, \"message\": \"Author added successfully.\"}");
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Please use yyyy-MM-dd.");
        }
    }
}

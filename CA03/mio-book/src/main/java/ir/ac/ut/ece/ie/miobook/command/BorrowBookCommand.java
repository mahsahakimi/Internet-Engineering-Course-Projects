package ir.ac.ut.ece.ie.miobook.command;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.ac.ut.ece.ie.miobook.service.BorrowBookService;

import java.io.IOException;

public class BorrowBookCommand {

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

        if (node.get("days") == null) {
            throw new IllegalArgumentException("Missing 'days' field.");
        }
        int days = node.get("days").asInt();

        BorrowBookService.borrowBook(username, title, days);
        System.out.println("{\"success\": true, \"message\": \"Book borrowed successfully.\"}");
    }
}
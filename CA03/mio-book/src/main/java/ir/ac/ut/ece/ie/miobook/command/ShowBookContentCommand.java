package ir.ac.ut.ece.ie.miobook.command;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.ac.ut.ece.ie.miobook.service.ShowBookContentService;

import java.io.IOException;

public class ShowBookContentCommand {

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

        String bookContent = ShowBookContentService.showBookContent(username, title);
        System.out.println(bookContent);
    }
}

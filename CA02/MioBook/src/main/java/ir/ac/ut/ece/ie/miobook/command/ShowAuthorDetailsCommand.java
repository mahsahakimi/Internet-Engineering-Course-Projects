package ir.ac.ut.ece.ie.miobook.command;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.ac.ut.ece.ie.miobook.database.AuthorDatabase;
import ir.ac.ut.ece.ie.miobook.model.Author;
import ir.ac.ut.ece.ie.miobook.model.Role;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ShowAuthorDetailsCommand {

    public static void execute(String inputJson) throws IOException {
        if (inputJson == null) {
            System.out.println("Input data for show_user_details command is missing.");
            return;
        }

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(inputJson);

            String username = node.get("username").asText();

            Author author = AuthorDatabase.getInstance().findAuthorByName(username);

            if (author == null) {
                System.out.println("{\"success\": false, \"message\": \"Author not found.\"}");
                return;
            }

            Map<String, Object> userData = new HashMap<>();
            userData.put("name", author.getName());
            userData.put("penName", author.getPenName());
            userData.put("nationality", author.getNationality());
            userData.put("born", author.getBorn());
            userData.put("died", author.getDied());


            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Author details retrieved successfully.");
            response.put("data", userData);

            System.out.println(mapper.writeValueAsString(response));

        } catch (Exception e) {
            System.out.println("{\"success\": false, \"message\": \"" + e.getMessage() + "\"}");
        }
    }
}

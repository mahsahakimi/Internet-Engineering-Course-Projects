package ir.ac.ut.ece.ie.miobook.command;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.ac.ut.ece.ie.miobook.database.AuthorDatabase;
import ir.ac.ut.ece.ie.miobook.model.Author;
import ir.ac.ut.ece.ie.miobook.database.UserDatabase;
import ir.ac.ut.ece.ie.miobook.model.Role;
import ir.ac.ut.ece.ie.miobook.model.User;


import java.io.IOException;

public class AddAuthorCommand {

    public static void execute(String inputJson) throws IOException {
        if (inputJson == null) {
            System.out.println("Input data for add_author command is missing.");
            return;
        }

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(inputJson);

            String username = node.get("username").asText();
            User whoAdded = UserDatabase.getInstance().findUserByUsername(username);
            if(whoAdded == null){
                System.out.println("username is wrong!");
                return;
            }
            else if ( whoAdded.getRole() == Role.customer){
                System.out.println("The user is not an admin!");
                return;
            }

            String name = node.get("name").asText();
            String penName = node.get("penName").asText();
            String nationality = node.get("nationality").asText();
            String born = node.get("born").asText();
            String died = node.has("died") ? node.get("died").asText() : null;

            if (name == null || name.isEmpty()) {
                throw new IllegalArgumentException("Missing required field: name.");
            }
            if (penName == null || penName.isEmpty()) {
                throw new IllegalArgumentException("Missing required field: penName.");
            }
            if (nationality == null || nationality.isEmpty()) {
                throw new IllegalArgumentException("Missing required field: nationality.");
            }

            Author author = new Author(name, penName, nationality, born, died);
            AuthorDatabase.getInstance().addAuthor(author);

            System.out.println("{\"success\": true, \"message\": \"Author added successfully.\"}");
        } catch (IllegalArgumentException e) {
            System.out.println("{\"success\": false, \"message\": \"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            System.out.println("{\"success\": false, \"message\": \"An error occurred: " + e.getMessage() + "\"}");
        }
    }
}

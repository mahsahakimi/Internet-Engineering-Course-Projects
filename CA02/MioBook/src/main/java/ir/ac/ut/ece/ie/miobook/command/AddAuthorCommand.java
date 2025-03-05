package ir.ac.ut.ece.ie.miobook.command;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.ac.ut.ece.ie.miobook.database.AuthorDatabase;
import ir.ac.ut.ece.ie.miobook.model.Author;
import ir.ac.ut.ece.ie.miobook.database.UserDatabase;
import ir.ac.ut.ece.ie.miobook.model.Role;
import ir.ac.ut.ece.ie.miobook.model.User;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

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
                throw new IllegalArgumentException("there is no user with this username!");
            }
            else if ( whoAdded.getRole() == Role.customer){
                throw new IllegalArgumentException("The user is not an admin!");
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
            if (born == null || born.isEmpty()) {
                throw new java.time.DateTimeException( name + " date is not valid...");
            }
            LocalDate bornDate = LocalDate.parse(born);
            LocalDate diedDate = null;
            if(died != null){
                diedDate = LocalDate.parse(died);
            }

            Author author = new Author(name, penName, nationality, bornDate, diedDate);
            AuthorDatabase.getInstance().addAuthor(author);

            System.out.println("{\"success\": true, \"message\": \"Author added successfully.\"}");
        }catch (DateTimeParseException e) {
            System.out.println("{\"success\": false, \"message\": \"An error occurred: " + "born date is not valid" + "\"}");
        }catch (IllegalArgumentException e) {
            System.out.println("{\"success\": false, \"message\": \"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            System.out.println("{\"success\": false, \"message\": \"An error occurred: " + e.getMessage() + "\"}");
        }
    }
}

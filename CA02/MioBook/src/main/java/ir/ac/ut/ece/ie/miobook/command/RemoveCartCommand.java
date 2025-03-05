package ir.ac.ut.ece.ie.miobook.command;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.ac.ut.ece.ie.miobook.database.BookDatabase;
import ir.ac.ut.ece.ie.miobook.database.UserDatabase;
import ir.ac.ut.ece.ie.miobook.model.Book;
import ir.ac.ut.ece.ie.miobook.model.Role;
import ir.ac.ut.ece.ie.miobook.model.User;

import java.io.IOException;

public class RemoveCartCommand {

    public static void execute(String inputJson) throws IOException {
        if (inputJson == null) {
            System.out.println("Input data for remove_cart command is missing.");
            return;
        }

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(inputJson);

            String username = node.get("username").asText();
            String title = node.get("title").asText();

            User user = UserDatabase.getInstance().findUserByUsername(username);
            Book book = BookDatabase.getInstance().findBookByTitle(title);

            if (user == null) {
                System.out.println("{\"success\": false, \"message\": \"User not found.\"}");
                return;
            }

            if (book == null) {
                System.out.println("{\"success\": false, \"message\": \"Book not found.\"}");
                return;
            }

            if (user.getRole() == Role.admin) {
                System.out.println("{\"success\": false, \"message\": \"Permission denied. Admin cannot remove from cart.\"}");
                return;
            }

            if (!user.getCart().contains(book)) {
                System.out.println("{\"success\": false, \"message\": \"Book not in cart.\"}");
                return;
            }

            user.getCart().remove(book);
            System.out.println("{\"success\": true, \"message\": \"Removed book from cart.\"}");

        } catch (Exception e) {
            System.out.println("{\"success\": false, \"message\": \"" + e.getMessage() + "\"}");
        }
    }
}

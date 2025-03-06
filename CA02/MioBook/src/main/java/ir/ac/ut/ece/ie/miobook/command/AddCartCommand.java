package ir.ac.ut.ece.ie.miobook.command;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.ac.ut.ece.ie.miobook.database.BookDatabase;
import ir.ac.ut.ece.ie.miobook.database.UserDatabase;
import ir.ac.ut.ece.ie.miobook.model.Book;
import ir.ac.ut.ece.ie.miobook.model.CartElement;
import ir.ac.ut.ece.ie.miobook.model.Role;
import ir.ac.ut.ece.ie.miobook.model.User;

import java.io.IOException;

public class AddCartCommand {

    public static void execute(String inputJson) throws IOException {
        if (inputJson == null) {
            System.out.println("Input data for add_cart command is missing.");
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
                System.out.println("{\"success\": false, \"message\": \"Permission denied. Admin cannot add to cart.\"}");
                return;
            }

            if (user.getCart().size() >= 10) {
                System.out.println("{\"success\": false, \"message\": \"Cart is full.\"}");
                return;
            }

            CartElement newElement = new CartElement(book, 0);
            user.addToCart(newElement);
            System.out.println("{\"success\": true, \"message\": \"Added book to cart.\"}");

        } catch (Exception e) {
            System.out.println("{\"success\": false, \"message\": \"" + e.getMessage() + "\"}");
        }
    }
}

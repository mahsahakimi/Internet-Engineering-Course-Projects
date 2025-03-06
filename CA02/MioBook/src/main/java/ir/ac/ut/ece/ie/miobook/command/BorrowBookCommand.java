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
import java.time.LocalDate;

public class BorrowBookCommand {

    public static void execute(String inputJson) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(inputJson);

        if (node.get("username") == null) {
            System.out.println("{\"success\": false, \"message\": \"Missing 'username' field.\"}");
            return;
        }

        if (node.get("title") == null) {
            System.out.println("{\"success\": false, \"message\": \"Missing 'title' field.\"}");
            return;
        }

        if (node.get("days") == null) {
            System.out.println("{\"success\": false, \"message\": \"Missing 'days' field.\"}");
            return;
        }

        String username = node.get("username").asText();
        User user = UserDatabase.getInstance().findUserByUsername(username);
        if (user == null) {
            System.out.println("{\"success\": false, \"message\": \"User not found.\"}");
            return;
        }

        if (user.getRole() != Role.customer) {
            System.out.println("{\"success\": false, \"message\": \"Only customers can borrow books.\"}");
            return;
        }

        String title = node.get("title").asText();
        Book book = BookDatabase.getInstance().findBookByTitle(title);
        if (book == null) {
            System.out.println("{\"success\": false, \"message\": \"Book not found.\"}");
            return;
        }
        int days = node.get("days").asInt();

        if (days < 1 || days > 9) {
            System.out.println("{\"success\": false, \"message\": \"Invalid number of days. Days must be between 1 and 9.\"}");
            return;
        }

        CartElement newElement = new CartElement(book, days);
        user.addToCart((newElement));

    }
}

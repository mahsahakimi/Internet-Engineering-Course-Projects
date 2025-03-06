package ir.ac.ut.ece.ie.miobook.command;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.ac.ut.ece.ie.miobook.database.UserDatabase;
import ir.ac.ut.ece.ie.miobook.model.Book;
import ir.ac.ut.ece.ie.miobook.model.CartElement;
import ir.ac.ut.ece.ie.miobook.model.Role;
import ir.ac.ut.ece.ie.miobook.model.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShowCartCommand {

    public static void execute(String inputJson) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(inputJson);

        if (node.get("username") == null) {
            System.out.println("{\"success\": false, \"message\": \"Missing 'username' field.\"}");
            return;
        }

        String username = node.get("username").asText();
        User user = UserDatabase.getInstance().findUserByUsername(username);

        if (user == null) {
            System.out.println("{\"success\": false, \"message\": \"User not found.\"}");
            return;
        }

        if (user.getRole() == Role.admin) {
            System.out.println("{\"success\": false, \"message\": \"Admins cannot have a cart.\"}");
            return;
        }

        int totalCost = 0;
        List<Map<String, Object>> items = new ArrayList<>();

        for (CartElement cartElement : user.getCart()) {
            Book book = cartElement.getBook();
            int days = cartElement.getDays();
            int finalPrice = cartElement.getPrice();

            totalCost += finalPrice;

            Map<String, Object> item = new HashMap<>();
            item.put("title", book.getTitle());
            item.put("author", book.getAuthor().getName());
            item.put("publisher", book.getPublisher());
            item.put("genres", book.getGenres());
            item.put("year", book.getYear());
            item.put("price", book.getPrice());
            item.put("isBorrowed", days != 0);
            item.put("finalPrice", finalPrice);
            if (days != 0) {
                item.put("borrowDays", days);
            }
            items.add(item);
        }

        Map<String, Object> data = new HashMap<>();
        data.put("username", username);
        data.put("totalCost", totalCost);
        data.put("items", items);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Buy cart retrieved successfully.");
        response.put("data", data);

        System.out.println(mapper.writeValueAsString(response));
    }
}

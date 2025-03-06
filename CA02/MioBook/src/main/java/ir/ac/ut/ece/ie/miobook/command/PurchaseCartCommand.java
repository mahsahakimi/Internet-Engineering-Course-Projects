package ir.ac.ut.ece.ie.miobook.command;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.ac.ut.ece.ie.miobook.database.UserDatabase;
import ir.ac.ut.ece.ie.miobook.model.Book;
import ir.ac.ut.ece.ie.miobook.model.Purchase;
import ir.ac.ut.ece.ie.miobook.model.Role;
import ir.ac.ut.ece.ie.miobook.model.User;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PurchaseCartCommand {

    public static void execute(String inputJson) throws IOException {
        if (inputJson == null) {
            System.out.println("Input data for purchase_cart command is missing.");
            return;
        }

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(inputJson);

            String username = node.get("username").asText();

            User user = UserDatabase.getInstance().findUserByUsername(username);

            if (user == null) {
                System.out.println("{\"success\": false, \"message\": \"User not found.\"}");
                return;
            }

            if (user.getRole() == Role.admin) {
                System.out.println("{\"success\": false, \"message\": \"Permission denied. Admin cannot purchase.\"}");
                return;
            }

            List<Book> cart = user.getCart();

            if (cart.isEmpty()) {
                System.out.println("{\"success\": false, \"message\": \"Cart is empty.\"}");
                return;
            }

            int totalCost = 0;
            int cartSize = cart.size();
            for (Book book : cart) {
                totalCost += book.getPrice();
            }

            if (user.getBalance() < totalCost) {
                System.out.println("{\"success\": false, \"message\": \"Insufficient balance.\"}");
                return;
            }

            user.setBalance(user.getBalance() - totalCost);
            List<String> bookTitles = new ArrayList<>();
            for(Book book : cart){
                bookTitles.add(book.getTitle());
            }

            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = now.format(formatter);

            Purchase purchase = new Purchase(bookTitles, totalCost, formattedDateTime);
            user.getPurchases().add(purchase);
            user.getCart().clear();

            System.out.println("{\"success\": true, \"message\": \"Purchase completed successfully.\", \"data\": {\"bookCount\": " + cartSize + ", \"totalCost\": " + totalCost + ", \"date\": \"" + formattedDateTime + "\"}}");

        } catch (Exception e) {
            System.out.println("{\"success\": false, \"message\": \"" + e.getMessage() + "\"}");
        }
    }
}

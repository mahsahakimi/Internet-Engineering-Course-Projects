package ir.ac.ut.ece.ie.miobook.command;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.ac.ut.ece.ie.miobook.database.UserDatabase;
import ir.ac.ut.ece.ie.miobook.model.Role;
import ir.ac.ut.ece.ie.miobook.model.User;

import java.io.IOException;

public class AddCreditCommand {

    public static void execute(String inputJson) throws IOException {
        if (inputJson == null) {
            System.out.println("Input data for add_credit command is missing.");
            return;
        }

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(inputJson);

            String username = node.get("username").asText();
            int credit = node.get("credit").asInt();

            if (credit <= 0) {
                System.out.println("{\"success\": false, \"message\": \"Credit must be greater than zero.\"}");
                return;
            }

            if (credit < 100) {
                System.out.println("{\"success\": false, \"message\": \"Minimum credit is 100 cents (1 dollar).\"}");
                return;
            }

            User user = UserDatabase.getInstance().findUserByUsername(username);

            if (user == null) {
                System.out.println("{\"success\": false, \"message\": \"User not found.\"}");
                return;
            }

            if (user.getRole() != Role.customer) {
                System.out.println("{\"success\": false, \"message\": \"This command is only for customer users.\"}");
                return;
            }

            user.setBalance(user.getBalance() + credit);
            System.out.println("{\"success\": true, \"message\": \"Credit added successfully.\"}");

        } catch (IllegalArgumentException e) {
            System.out.println("{\"success\": false, \"message\": \"" + e.getMessage() + "\"}");
        }
    }
}

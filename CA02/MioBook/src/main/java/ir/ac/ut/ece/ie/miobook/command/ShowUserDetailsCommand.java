package ir.ac.ut.ece.ie.miobook.command;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.ac.ut.ece.ie.miobook.database.UserDatabase;
import ir.ac.ut.ece.ie.miobook.model.Role;
import ir.ac.ut.ece.ie.miobook.model.User;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ShowUserDetailsCommand {

    public static void execute(String inputJson) throws IOException {
        if (inputJson == null) {
            System.out.println("Input data for show_user_details command is missing.");
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

            Map<String, Object> userData = new HashMap<>();
            userData.put("username", user.getUsername());
            userData.put("role", user.getRole());
            userData.put("email", user.getEmail());
            userData.put("address", user.getAddress());

            if (user.getRole() == Role.customer) {
                userData.put("balance", user.getBalance());
            }

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "User details retrieved successfully.");
            response.put("data", userData);

            System.out.println(mapper.writeValueAsString(response));

        } catch (IllegalArgumentException e) {
            System.out.println("{\"success\": false, \"message\": \"" + e.getMessage() + "\"}");
        }
    }
}

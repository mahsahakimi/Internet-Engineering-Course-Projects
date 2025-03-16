package ir.ac.ut.ece.ie.miobook.command;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.ac.ut.ece.ie.miobook.model.Address;
import ir.ac.ut.ece.ie.miobook.service.AddUserService;

import java.io.IOException;

public class AddUserCommand {

    public static void execute(String inputJson) throws IOException {
        if (inputJson == null) {
            throw new IllegalArgumentException("Input data for add_user command is missing.");
        }

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(inputJson);

        // Check each field
        if (node.get("role") == null || node.get("username") == null || node.get("password") == null ||
                node.get("email") == null || node.get("address") == null) {
            throw new IllegalArgumentException("Missing required fields.");
        }

        // Check required address fields
        JsonNode addressNode = node.get("address");
        if (addressNode.get("country") == null || addressNode.get("city") == null) {
            throw new IllegalArgumentException("Missing required address fields (country or city).");
        }

        String role = node.get("role").asText();
        String username = node.get("username").asText();
        String password = node.get("password").asText();
        String email = node.get("email").asText();
        Address address = mapper.treeToValue(node.get("address"), Address.class);

        // Call the service to do the business logic
        AddUserService.addUser(role, username, password, email, address);

        System.out.println("{\"success\": true, \"message\": \"User added successfully.\"}");
    }
}
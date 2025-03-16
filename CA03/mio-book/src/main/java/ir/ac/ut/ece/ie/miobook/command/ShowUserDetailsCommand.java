package ir.ac.ut.ece.ie.miobook.command;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.ac.ut.ece.ie.miobook.service.ShowUserDetailsService;
import java.io.IOException;

public class ShowUserDetailsCommand {

    public static void execute(String inputJson) throws IOException {
        if (inputJson == null) {
            throw new IllegalArgumentException("Input data for show_user_details command is missing.");
        }

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(inputJson);

        // Check required fields
        if (node.get("username") == null) {
            throw new IllegalArgumentException("Missing required fields.");
        }

        String username = node.get("username").asText();

        // Call the service to do the business logic
        String responseJson = ShowUserDetailsService.getUserDetails(username);

        System.out.println(responseJson);
    }
}

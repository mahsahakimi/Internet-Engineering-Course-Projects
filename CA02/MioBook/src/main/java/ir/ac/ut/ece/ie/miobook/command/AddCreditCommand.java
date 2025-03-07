package ir.ac.ut.ece.ie.miobook.command;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.ac.ut.ece.ie.miobook.service.AddCreditService;

import java.io.IOException;

public class AddCreditCommand {

    public static void execute(String inputJson) throws IOException {
        if (inputJson == null) {
            throw new IllegalArgumentException("Input data for add_credit command is missing.");
        }

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(inputJson);

        // Check required fields
        if (node.get("username") == null || node.get("credit") == null) {
            throw new IllegalArgumentException("Missing required fields.");
        }

        String username = node.get("username").asText();
        int credit = node.get("credit").asInt();

        // Call the service to do the business logic
        AddCreditService.addCredit(username, credit);

        System.out.println("{\"success\": true, \"message\": \"Credit added successfully.\"}");
    }
}
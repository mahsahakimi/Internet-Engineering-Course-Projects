package ir.ac.ut.ece.ie.miobook.command;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.ac.ut.ece.ie.miobook.service.ShowPurchaseHistoryService;

import java.io.IOException;

public class ShowPurchaseHistoryCommand {

    public static void execute(String inputJson) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(inputJson);

        if (node.get("username") == null) {
            throw new IllegalArgumentException("Missing 'username' field.");
        }
        String username = node.get("username").asText();

        String purchaseHistory = ShowPurchaseHistoryService.showPurchaseHistory(username);
        System.out.println(purchaseHistory);
    }
}
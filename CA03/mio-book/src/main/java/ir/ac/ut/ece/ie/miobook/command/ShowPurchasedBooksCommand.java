package ir.ac.ut.ece.ie.miobook.command;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.ac.ut.ece.ie.miobook.service.ShowPurchasedBooksService;

import java.io.IOException;

public class ShowPurchasedBooksCommand {

    public static void execute(String inputJson) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(inputJson);

        if (node.get("username") == null) {
            throw new IllegalArgumentException("Missing 'username' field.");
        }
        String username = node.get("username").asText();

        String purchasedBooks = ShowPurchasedBooksService.showPurchasedBooks(username);
        System.out.println(purchasedBooks);
    }
}

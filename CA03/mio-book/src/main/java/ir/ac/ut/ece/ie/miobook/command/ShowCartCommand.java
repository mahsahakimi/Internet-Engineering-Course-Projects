package ir.ac.ut.ece.ie.miobook.command;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.ac.ut.ece.ie.miobook.service.ShowCartService;

import java.io.IOException;

public class ShowCartCommand {

    public static void execute(String inputJson) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(inputJson);

        if (node.get("username") == null) {
            throw new IllegalArgumentException("Missing 'username' field.");
        }
        String username = node.get("username").asText();

        String cartData = ShowCartService.showCart(username);
        System.out.println(cartData);
    }
}
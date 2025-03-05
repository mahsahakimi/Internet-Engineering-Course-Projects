package ir.ac.ut.ece.ie.miobook;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.ac.ut.ece.ie.miobook.database.UserDatabase;
import ir.ac.ut.ece.ie.miobook.model.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line = reader.readLine();
        String[] parts = line.split(" ", 2);
        String command = parts[0];
        String inputJson = parts.length > 1 ? parts[1] : null;

        if (command.equals("add_user")) {
            addUser(inputJson);
        } else {
            System.out.println("Invalid command.");
        }
    }

    private static void addUser(String inputJson) throws IOException {
        if (inputJson == null) {
            System.out.println("Input data for add_user command is missing.");
            return;
        }

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(inputJson);

            User user = mapper.treeToValue(node, User.class);
            UserDatabase.getInstance().addUser(user);
//            System.out.println(user);

            System.out.println("{\"success\": true, \"message\": \"User added successfully.\"}");
        } catch (IllegalArgumentException e) {
            System.out.println("{\"success\": false, \"message\": \"" + e.getMessage() + "\"}");
        }
    }
}

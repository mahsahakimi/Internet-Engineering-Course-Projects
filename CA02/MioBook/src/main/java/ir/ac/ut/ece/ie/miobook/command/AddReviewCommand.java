package ir.ac.ut.ece.ie.miobook.command;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.ac.ut.ece.ie.miobook.database.ReviewDatabase;
import ir.ac.ut.ece.ie.miobook.database.UserDatabase;
import ir.ac.ut.ece.ie.miobook.model.Review;
import ir.ac.ut.ece.ie.miobook.model.Role;
import ir.ac.ut.ece.ie.miobook.model.User;

import java.io.IOException;

public class AddReviewCommand {
    public static void execute(String inputJson) throws IOException {
        if (inputJson == null) {
            System.out.println("{\"success\": false, \"message\": \"Input data for add_user command is missing.\"}");
            return;
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(inputJson);

            String username = node.get("username").asText();
            User user = UserDatabase.getInstance().findUserByUsername(username);
            if (user.getRole() == Role.admin) {
                System.out.println("{\"success\": false, \"message\": \"Permission denied.\"}");
                return;
            }
            String title = node.get("title").asText();
            int rate = node.get("rate").asInt();
            String comment = node.get("comment").asText();
            Review review = new Review(username, title, rate, comment);

            ReviewDatabase.getInstance().addReview(review);
            System.out.println("{\"success\": true, \"message\": \"\"Review added successfully.\"}");


        } catch (Exception e) {
            System.out.println("{\"success\": false, \"message\": \"" + e.getMessage() + "\"}");
        }
    }
}

package ir.ac.ut.ece.ie.miobook.command;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.ac.ut.ece.ie.miobook.database.AuthorDatabase;
import ir.ac.ut.ece.ie.miobook.database.BookDatabase;
import ir.ac.ut.ece.ie.miobook.database.UserDatabase;
import ir.ac.ut.ece.ie.miobook.model.Author;
import ir.ac.ut.ece.ie.miobook.model.Book;
import ir.ac.ut.ece.ie.miobook.model.Role;
import ir.ac.ut.ece.ie.miobook.model.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AddBookCommand {

    public static void execute(String inputJson) throws IOException {
        if (inputJson == null) {
            System.out.println("Input data for add_book command is missing.");
            return;
        }

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(inputJson);

            String username = node.get("username").asText();
            String title = node.get("title").asText();
            String authorName = node.get("author").asText();
            String publisher = node.get("publisher").asText();
            int year = node.get("year").asInt();
            double price = node.get("price").asDouble();
            String synopsis = node.get("synopsis").asText();
            String content = node.get("content").asText();

            JsonNode genresNode = node.get("genres");
            List<String> genres = new ArrayList<>();
            for (JsonNode genreNode : genresNode) {
                genres.add(genreNode.asText());
            }

            User user = UserDatabase.getInstance().findUserByUsername(username);

            if (user == null || user.getRole() != Role.admin) {
                System.out.println("{\"success\": false, \"message\": \"Permission denied.\"}");
                return;
            }

            Author author = AuthorDatabase.getInstance().findAuthorByName(authorName);

            if (author == null) {
                System.out.println("{\"success\": false, \"message\": \"Author not found.\"}");
                return;
            }

            Book book = new Book(title, author, publisher, year, genres, price, synopsis, content);
            BookDatabase.getInstance().addBook(book);

            System.out.println("{\"success\": true, \"message\": \"Book added successfully.\"}");

        } catch (IllegalArgumentException e) {
            System.out.println("{\"success\": false, \"message\": \"" + e.getMessage() + "\"}");
        }
    }
}

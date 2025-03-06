package ir.ac.ut.ece.ie.miobook.command;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.ac.ut.ece.ie.miobook.database.UserDatabase;
import ir.ac.ut.ece.ie.miobook.model.Book;
import ir.ac.ut.ece.ie.miobook.model.Role;
import ir.ac.ut.ece.ie.miobook.model.User;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShowPurchasedBooksCommand {

    public static void execute(String inputJson) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(inputJson);

        if (node.get("username") == null) {
            System.out.println("{\"success\": false, \"message\": \"Missing 'username' field.\"}");
            return;
        }

        String username = node.get("username").asText();
        User user = UserDatabase.getInstance().findUserByUsername(username);

        if (user == null) {
            System.out.println("{\"success\": false, \"message\": \"User not found.\"}");
            return;
        }

        if (user.getRole() == Role.admin) {
            System.out.println("{\"success\": false, \"message\": \"Admins do not have purchased books.\"}");
            return;
        }

        List<Map<String, Object>> books = new ArrayList<>();

        // Add owned books
        for (Book book : user.getOwnedBooks()) {
            Map<String, Object> bookData = new HashMap<>();
            bookData.put("title", book.getTitle());
            bookData.put("author", book.getAuthor().getName());
            bookData.put("publisher", book.getPublisher());
            bookData.put("category", book.getGenres());
            bookData.put("year", book.getYear());
            bookData.put("price", book.getPrice());
            bookData.put("isBorrowed", false);
            books.add(bookData);
        }

        // Add borrowed books that are not expired
        for (Map.Entry<Book, LocalDate> entry : user.getBorrowedBooks().entrySet()) {
            Book book = entry.getKey();
            LocalDate dueDate = entry.getValue();
            if (dueDate.isAfter(LocalDate.now())) {
                Map<String, Object> bookData = new HashMap<>();
                bookData.put("title", book.getTitle());
                bookData.put("author", book.getAuthor().getName());
                bookData.put("publisher", book.getPublisher());
                bookData.put("category", book.getGenres());
                bookData.put("year", book.getYear());
                bookData.put("price", book.getPrice());
                bookData.put("isBorrowed", true);
                books.add(bookData);
            }
        }

        Map<String, Object> data = new HashMap<>();
        data.put("username", username);
        data.put("books", books);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Purchased books retrieved successfully.");
        response.put("data", data);

        System.out.println(mapper.writeValueAsString(response));
    }
}

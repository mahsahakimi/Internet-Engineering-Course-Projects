package ir.ac.ut.ece.ie.miobook.command;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.ac.ut.ece.ie.miobook.database.BookDatabase;
import ir.ac.ut.ece.ie.miobook.database.UserDatabase;
import ir.ac.ut.ece.ie.miobook.model.Book;
import ir.ac.ut.ece.ie.miobook.model.Role;
import ir.ac.ut.ece.ie.miobook.model.User;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShowBookContentCommand
{
    public static void execute(String inputJson) throws IOException {
        if (inputJson == null) {
            System.out.println("Input data for show_author_details command is missing.");
            return;
        }

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(inputJson);

            String username = node.get("username").asText();
            User user = UserDatabase.getInstance().findUserByUsername(username);
            if(user == null){
                throw new Exception("User does not exist!");
            }
            if(user.getRole() == Role.admin){
                throw new Exception("You are admin!");
            }
            String title = node.get("title").asText();
            Book book = BookDatabase.getInstance().findBookByTitle(title);
            if(book == null){
                throw new Exception("Book does not exist!");
            }
            List<Book> books = user.getOwnedBooks();
            for(Book b : books){
                if(b.getTitle() == title){
                    Map<String, Object> data = new HashMap<>();
                    data.put("title", b.getTitle());
                    data.put("content", b.getContent());

                    Map<String, Object> response = new HashMap<>();
                    response.put("success", true);
                    response.put("message", "Book content retrieved successfully");
                    response.put("data", data);

                    System.out.println(mapper.writeValueAsString(response));
                    return;
                }
            }
            throw new Exception("User does not have access to the book!");


        } catch (Exception e) {
            System.out.println("{\"success\": false, \"message\": \"" + e.getMessage() + "\"}");
        }
    }
}

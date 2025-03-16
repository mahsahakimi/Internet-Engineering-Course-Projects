package ir.ac.ut.ece.ie.miobook.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import ir.ac.ut.ece.ie.miobook.database.AuthorDatabase;
import ir.ac.ut.ece.ie.miobook.model.Author;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ShowAuthorDetailsService {

    public static String getAuthorDetails(String authorName) throws IOException {
        Author author = AuthorDatabase.getInstance().findAuthorByName(authorName);

        if (author == null) {
            throw new IllegalArgumentException("Author not found.");
        }

        Map<String, Object> authorData = new HashMap<>();
        authorData.put("name", author.getName());
        authorData.put("penName", author.getPenName());
        authorData.put("nationality", author.getNationality());
        authorData.put("born", author.getBorn().toString());

        // Only add "died" if it's not null
        if (author.getDied() != null) {
            authorData.put("died", author.getDied().toString());
        }

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Author details retrieved successfully.");
        response.put("data", authorData);

        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(response);
    }
}

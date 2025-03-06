package ir.ac.ut.ece.ie.miobook.command;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.ac.ut.ece.ie.miobook.database.AuthorDatabase;
import ir.ac.ut.ece.ie.miobook.database.BookDatabase;
import ir.ac.ut.ece.ie.miobook.model.Book;

import java.io.IOException;
import java.util.List;

public class SearchBooksCommand {

    public static void searchByTitle(String inputJson) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(inputJson);

        if (node.get("title") == null) {
            System.out.println("{\"success\": false, \"message\": \"Missing 'title' field.\"}");
            return;
        }
        String title = node.get("title").asText();

        List<Book> books = BookDatabase.getInstance().findBooksByTitle(title);
        if (books.isEmpty()) {
            System.out.println("{\"success\": false, \"message\": \"No books found.\"}");
            return;
        }

        System.out.println("{\"success\": true, \"message\": \"Books containing '" + title + "' in their title:\", \"data\": {\"search\": \"" + title + "\", \"books\": " + mapper.writeValueAsString(books) + "}}");
    }

    public static void searchByAuthor(String inputJson) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(inputJson);

        if (node.get("name") == null) {
            System.out.println("{\"success\": false, \"message\": \"Missing 'name' field.\"}");
            return;
        }
        String authorName = node.get("name").asText();

        if (AuthorDatabase.getInstance().findAuthorByName(authorName) == null) {
            System.out.println("{\"success\": false, \"message\": \"Author not found.\"}");
            return;
        }

        List<Book> books = BookDatabase.getInstance().findBooksByAuthor(authorName);
        if (books.isEmpty()) {
            System.out.println("{\"success\": false, \"message\": \"No books found by this author.\"}");
            return;
        }

        System.out.println("{\"success\": true, \"message\": \"Books by '" + authorName + "':\", \"data\": {\"search\": \"" + authorName + "\", \"books\": " + mapper.writeValueAsString(books) + "}}");
    }

    public static void searchByGenre(String inputJson) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(inputJson);

        if (node.get("genre") == null) {
            System.out.println("{\"success\": false, \"message\": \"Missing 'genre' field.\"}");
            return;
        }
        String genre = node.get("genre").asText();

        List<Book> books = BookDatabase.getInstance().findBooksByGenre(genre);
        if (books.isEmpty()) {
            System.out.println("{\"success\": false, \"message\": \"No books found.\"}");
            return;
        }

        System.out.println("{\"success\": true, \"message\": \"Books in the '" + genre + "' genre:\", \"data\": {\"search\": \"" + genre + "\", \"books\": " + mapper.writeValueAsString(books) + "}}");
    }

    public static void searchByYear(String inputJson) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(inputJson);

        if (node.get("from") == null || node.get("to") == null) {
            System.out.println("{\"success\": false, \"message\": \"Missing 'from' or 'to' field.\"}");
            return;
        }
        int fromYear = node.get("from").asInt();
        int toYear = node.get("to").asInt();

        if (fromYear > toYear) {
            System.out.println("{\"success\": false, \"message\": \"Invalid year range.\"}");
            return;
        }

        List<Book> books = BookDatabase.getInstance().findBooksByYear(fromYear, toYear);
        if (books.isEmpty()) {
            System.out.println("{\"success\": false, \"message\": \"No books found.\"}");
            return;
        }

        System.out.println("{\"success\": true, \"message\": \"Books published from " + fromYear + " to " + toYear + ":\", \"data\": {\"search\": \"" + fromYear + "-" + toYear + "\", \"books\": " + mapper.writeValueAsString(books) + "}}");
    }
}

package ir.ac.ut.ece.ie.miobook.command;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.ac.ut.ece.ie.miobook.service.SearchBooksService;
import java.io.IOException;

public class SearchBooksCommand {

    public static void searchByTitle(String inputJson) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(inputJson);

        if (node.get("title") == null) {
            throw new IllegalArgumentException("Missing 'title' field.");
        }
        String title = node.get("title").asText();

        String responseJson = SearchBooksService.searchByTitle(title);
        System.out.println(responseJson);
    }

    public static void searchByAuthor(String inputJson) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(inputJson);

        if (node.get("name") == null) {
            throw new IllegalArgumentException("Missing 'name' field.");
        }
        String authorName = node.get("name").asText();

        String responseJson = SearchBooksService.searchByAuthor(authorName);
        System.out.println(responseJson);
    }

    public static void searchByGenre(String inputJson) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(inputJson);

        if (node.get("genre") == null) {
            throw new IllegalArgumentException("Missing 'genre' field.");
        }
        String genre = node.get("genre").asText();

        String responseJson = SearchBooksService.searchByGenre(genre);
        System.out.println(responseJson);
    }

    public static void searchByYear(String inputJson) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(inputJson);

        if (node.get("from") == null || node.get("to") == null) {
            throw new IllegalArgumentException("Missing 'from' or 'to' field.");
        }
        int fromYear = node.get("from").asInt();
        int toYear = node.get("to").asInt();

        if (fromYear > toYear) {
            throw new IllegalArgumentException("Invalid year range.");
        }

        String responseJson = SearchBooksService.searchByYear(fromYear, toYear);
        System.out.println(responseJson);
    }
}

package ir.ac.ut.ece.ie.miobook.command;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.ac.ut.ece.ie.miobook.service.SearchBooksService;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class SearchBooksCommand {

    public static void searchByTitle(String inputJson) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(inputJson);

        if (node.get("title") == null) {
            throw new IllegalArgumentException("Missing 'title' field.");
        }
        String title = node.get("title").asText();
        int page = node.has("page") && node.get("page").isInt() ? node.get("page").asInt() : 1;
        int pageSize = node.has("pageSize") && node.get("pageSize").isInt() ? node.get("pageSize").asInt() : 10;

        String responseJson = SearchBooksService.searchByTitle(title, page, pageSize);
        System.out.println(responseJson);
    }

    public static void searchByAuthor(String inputJson) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(inputJson);

        if (node.get("name") == null) {
            throw new IllegalArgumentException("Missing 'name' field.");
        }
        String authorName = node.get("name").asText();
        int page = node.has("page") && node.get("page").isInt() ? node.get("page").asInt() : 1;
        int pageSize = node.has("pageSize") && node.get("pageSize").isInt() ? node.get("pageSize").asInt() : 10;

        String responseJson = SearchBooksService.searchByAuthor(authorName, page, pageSize);
        System.out.println(responseJson);
    }

    public static void searchByGenre(String inputJson) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(inputJson);

        if (node.get("genre") == null) {
            throw new IllegalArgumentException("Missing 'genre' field.");
        }
        String genre = node.get("genre").asText();
        int page = node.has("page") && node.get("page").isInt() ? node.get("page").asInt() : 1;
        int pageSize = node.has("pageSize") && node.get("pageSize").isInt() ? node.get("pageSize").asInt() : 10;

        String responseJson = SearchBooksService.searchByGenre(genre, page, pageSize);
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
        int page = node.has("page") && node.get("page").isInt() ? node.get("page").asInt() : 1;
        int pageSize = node.has("pageSize") && node.get("pageSize").isInt() ? node.get("pageSize").asInt() : 10;

        String responseJson = SearchBooksService.searchByYear(fromYear, toYear, page, pageSize);
        System.out.println(responseJson);
    }

    public static void searchBooks(String inputJson) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(inputJson);

        String title = node.has("title") && !node.get("title").isNull() ? node.get("title").asText() : null;
        String authorName = node.has("author") && !node.get("author").isNull() ? node.get("author").asText() : null;
        String genre = node.has("genre") && !node.get("genre").isNull() ? node.get("genre").asText() : null;
        Integer fromYear = node.has("fromYear") && node.get("fromYear").isInt() ? node.get("fromYear").asInt() : null;
        Integer toYear = node.has("toYear") && node.get("toYear").isInt() ? node.get("toYear").asInt() : null;
        String sortBy = node.has("sortBy") && !node.get("sortBy").isNull() ? node.get("sortBy").asText() : null;
        String sortOrder = node.has("sortOrder") && !node.get("sortOrder").isNull() ? node.get("sortOrder").asText() : null;
        int page = node.has("page") && node.get("page").isInt() ? node.get("page").asInt() : 1;
        int pageSize = node.has("pageSize") && node.get("pageSize").isInt() ? node.get("pageSize").asInt() : 10;

        if (fromYear != null && toYear != null && fromYear > toYear) {
            throw new IllegalArgumentException("Invalid year range.");
        }

        // Check for valid sortBy and sortOrder
        List<String> validSortBy = Arrays.asList("averageRating", "reviewCount", null);
        List<String> validSortOrder = Arrays.asList("asc", "desc", null);

        if (!validSortBy.contains(sortBy)) {
            throw new IllegalArgumentException("Invalid sortBy value. Allowed values: averageRating, reviewCount.");
        }

        if (!validSortOrder.contains(sortOrder)) {
            throw new IllegalArgumentException("Invalid sortOrder value. Allowed values: asc, desc.");
        }

        String responseJson = SearchBooksService.searchBooks(title, authorName, genre, fromYear, toYear, sortBy, sortOrder, page, pageSize);
        System.out.println(responseJson);
    }
}

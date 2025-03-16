package ir.ac.ut.ece.ie.miobook.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import ir.ac.ut.ece.ie.miobook.database.AuthorDatabase;
import ir.ac.ut.ece.ie.miobook.database.BookDatabase;
import ir.ac.ut.ece.ie.miobook.model.Book;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchBooksService {

    public static String searchByTitle(String title) throws IOException {
        List<Book> books = BookDatabase.getInstance().findBooksByTitle(title);
        if (books.isEmpty()) {
            throw new IllegalArgumentException("No books found.");
        }

        ObjectMapper mapper = new ObjectMapper();

        List<Map<String, Object>> formattedBooks = new ArrayList<>();
        for (Book book : books) {
            Map<String, Object> formattedBook = new HashMap<>();
            formattedBook.put("title", book.getTitle());
            formattedBook.put("author", book.getAuthor().getName());
            formattedBook.put("publisher", book.getPublisher());
            formattedBook.put("genres", book.getGenres());
            formattedBook.put("year", book.getYear());
            formattedBook.put("price", book.getPrice());
            formattedBook.put("synopsis", book.getSynopsis());
            formattedBooks.add(formattedBook);
        }
        return "{\"success\": true, \"message\": \"Books containing '" + title + "' in their title:\", \"data\": {\"search\": \"" + title + "\", \"books\": " + mapper.writeValueAsString(formattedBooks) + "}}";
    }

    public static String searchByAuthor(String authorName) throws IOException {
        if (AuthorDatabase.getInstance().findAuthorByName(authorName) == null) {
            throw new IllegalArgumentException("Author not found.");
        }

        List<Book> books = BookDatabase.getInstance().findBooksByAuthor(authorName);
        if (books.isEmpty()) {
            throw new IllegalArgumentException("No books found by this author.");
        }

        ObjectMapper mapper = new ObjectMapper();

        List<Map<String, Object>> formattedBooks = new ArrayList<>();
        for (Book book : books) {
            Map<String, Object> formattedBook = new HashMap<>();
            formattedBook.put("title", book.getTitle());
            formattedBook.put("author", book.getAuthor().getName());
            formattedBook.put("publisher", book.getPublisher());
            formattedBook.put("genres", book.getGenres());
            formattedBook.put("year", book.getYear());
            formattedBook.put("price", book.getPrice());
            formattedBook.put("synopsis", book.getSynopsis());
            formattedBooks.add(formattedBook);
        }
        return "{\"success\": true, \"message\": \"Books by '" + authorName + "':\", \"data\": {\"search\": \"" + authorName + "\", \"books\": " + mapper.writeValueAsString(formattedBooks) + "}}";
    }

    public static String searchByGenre(String genre) throws IOException {
        List<Book> books = BookDatabase.getInstance().findBooksByGenre(genre);
        if (books.isEmpty()) {
            throw new IllegalArgumentException("No books found.");
        }

        ObjectMapper mapper = new ObjectMapper();

        List<Map<String, Object>> formattedBooks = new ArrayList<>();
        for (Book book : books) {
            Map<String, Object> formattedBook = new HashMap<>();
            formattedBook.put("title", book.getTitle());
            formattedBook.put("author", book.getAuthor().getName());
            formattedBook.put("publisher", book.getPublisher());
            formattedBook.put("genres", book.getGenres());
            formattedBook.put("year", book.getYear());
            formattedBook.put("price", book.getPrice());
            formattedBook.put("synopsis", book.getSynopsis());
            formattedBooks.add(formattedBook);
        }
        return "{\"success\": true, \"message\": \"Books in the '" + genre + "' genre:\", \"data\": {\"search\": \"" + genre + "\", \"books\": " + mapper.writeValueAsString(formattedBooks) + "}}";
    }

    public static String searchByYear(int fromYear, int toYear) throws IOException {
        List<Book> books = BookDatabase.getInstance().findBooksByYear(fromYear, toYear);
        if (books.isEmpty()) {
            throw new IllegalArgumentException("No books found.");
        }

        ObjectMapper mapper = new ObjectMapper();

        List<Map<String, Object>> formattedBooks = new ArrayList<>();
        for (Book book : books) {
            Map<String, Object> formattedBook = new HashMap<>();
            formattedBook.put("title", book.getTitle());
            formattedBook.put("author", book.getAuthor().getName());
            formattedBook.put("publisher", book.getPublisher());
            formattedBook.put("genres", book.getGenres());
            formattedBook.put("year", book.getYear());
            formattedBook.put("price", book.getPrice());
            formattedBook.put("synopsis", book.getSynopsis());
            formattedBooks.add(formattedBook);
        }
        return "{\"success\": true, \"message\": \"Books published from " + fromYear + " to " + toYear + ":\", \"data\": {\"search\": \"" + fromYear + "-" + toYear + "\", \"books\": " + mapper.writeValueAsString(formattedBooks) + "}}";
    }
}
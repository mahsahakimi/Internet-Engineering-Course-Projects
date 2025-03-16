package ir.ac.ut.ece.ie.miobook.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import ir.ac.ut.ece.ie.miobook.database.AuthorDatabase;
import ir.ac.ut.ece.ie.miobook.database.BookDatabase;
import ir.ac.ut.ece.ie.miobook.database.ReviewDatabase;
import ir.ac.ut.ece.ie.miobook.model.Book;
import ir.ac.ut.ece.ie.miobook.model.Review;
import ir.ac.ut.ece.ie.miobook.utils.PaginationUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SearchBooksService {

    public static String searchByTitle(String title, int page, int pageSize) throws IOException {
        List<Book> books = BookDatabase.getInstance().findBooksByTitle(title);
        if (books.isEmpty()) {
            throw new IllegalArgumentException("No books found.");
        }

        // Apply pagination
        List<Book> paginatedBooks = PaginationUtils.paginate(books, page, pageSize);

        ObjectMapper mapper = new ObjectMapper();
        List<Map<String, Object>> formattedBooks = formatBooks(paginatedBooks);
        // TODO: add page and page size if needed!
        return "{\"success\": true, \"message\": \"Books containing '" + title + "' in their title:\", \"data\": {\"search\": \"" + title + "\", \"books\": " + mapper.writeValueAsString(formattedBooks) + "}}";
    }

    public static String searchByAuthor(String authorName, int page, int pageSize) throws IOException {
        if (AuthorDatabase.getInstance().findAuthorByName(authorName) == null) {
            throw new IllegalArgumentException("Author not found.");
        }

        List<Book> books = BookDatabase.getInstance().findBooksByAuthor(authorName);
        if (books.isEmpty()) {
            throw new IllegalArgumentException("No books found by this author.");
        }

        // Apply pagination
        List<Book> paginatedBooks = PaginationUtils.paginate(books, page, pageSize);

        ObjectMapper mapper = new ObjectMapper();
        List<Map<String, Object>> formattedBooks = formatBooks(paginatedBooks);
        // TODO: add page and page size if needed!
        return "{\"success\": true, \"message\": \"Books by '" + authorName + "':\", \"data\": {\"search\": \"" + authorName + "\", \"books\": " + mapper.writeValueAsString(formattedBooks) + "}}";
    }

    public static String searchByGenre(String genre, int page, int pageSize) throws IOException {
        List<Book> books = BookDatabase.getInstance().findBooksByGenre(genre);
        if (books.isEmpty()) {
            throw new IllegalArgumentException("No books found.");
        }

        // Apply pagination
        List<Book> paginatedBooks = PaginationUtils.paginate(books, page, pageSize);

        ObjectMapper mapper = new ObjectMapper();
        List<Map<String, Object>> formattedBooks = formatBooks(paginatedBooks);
        // TODO: add page and page size if needed!
        return "{\"success\": true, \"message\": \"Books in the '" + genre + "' genre:\", \"data\": {\"search\": \"" + genre + "\", \"books\": " + mapper.writeValueAsString(formattedBooks) + "}}";
    }

    public static String searchByYear(int fromYear, int toYear, int page, int pageSize) throws IOException {
        List<Book> books = BookDatabase.getInstance().findBooksByYear(fromYear, toYear);
        if (books.isEmpty()) {
            throw new IllegalArgumentException("No books found.");
        }

        // Apply pagination
        List<Book> paginatedBooks = PaginationUtils.paginate(books, page, pageSize);

        ObjectMapper mapper = new ObjectMapper();
        List<Map<String, Object>> formattedBooks = formatBooks(paginatedBooks);
        // TODO: add page and page size if needed!
        return "{\"success\": true, \"message\": \"Books published from " + fromYear + " to " + toYear + ":\", \"data\": {\"search\": \"" + fromYear + "-" + toYear + "\", \"books\": " + mapper.writeValueAsString(formattedBooks) + "}}";
    }

    public static String searchBooks(String title, String authorName, String genre, Integer fromYear, Integer toYear, String sortBy, String sortOrder, int page, int pageSize) throws IOException {
        List<Book> books = BookDatabase.getInstance().getBooks();

        // Apply filters
        if (title != null && !title.isEmpty()) {
            books = books.stream().filter(book -> book.getTitle().toLowerCase().contains(title.toLowerCase())).collect(Collectors.toList());
        }
        if (authorName != null && !authorName.isEmpty()) {
            books = books.stream().filter(book -> book.getAuthor().getName().toLowerCase().contains(authorName.toLowerCase())).collect(Collectors.toList());
        }
        if (genre != null && !genre.isEmpty()) {
            books = books.stream().filter(book -> book.getGenres().stream().anyMatch(g -> g.toLowerCase().equals(genre.toLowerCase()))).collect(Collectors.toList());
        }
        if (fromYear != null && toYear != null) {
            books = books.stream().filter(book -> book.getYear() >= fromYear && book.getYear() <= toYear).collect(Collectors.toList());
        }

        // Apply sorting
        if (sortBy != null && !sortBy.isEmpty()) {
            if (sortBy.equalsIgnoreCase("averageRating")) {
                books = sortBooksByAverageRating(books, sortOrder);
            } else if (sortBy.equalsIgnoreCase("reviewCount")) {
                books = sortBooksByReviewCount(books, sortOrder);
            }
        }

        // Apply pagination
        List<Book> paginatedBooks = PaginationUtils.paginate(books, page, pageSize);

        // Format books for output
        ObjectMapper mapper = new ObjectMapper();
        List<Map<String, Object>> formattedBooks = formatBooks(paginatedBooks);

        // Build response
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("books", formattedBooks);
        responseData.put("search", buildSearchString(title, authorName, genre, fromYear, toYear, sortBy, sortOrder));

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Search results:");
        // TODO: add page and page size if needed!
        response.put("data", responseData);

        return mapper.writeValueAsString(response);
    }

    private static List<Book> sortBooksByAverageRating(List<Book> books, String sortOrder) {
        return books.stream().sorted((book1, book2) -> {
            double rating1 = ReviewDatabase.getInstance().calculateAverageRating(book1);
            double rating2 = ReviewDatabase.getInstance().calculateAverageRating(book2);
            return sortOrder.equalsIgnoreCase("asc") ? Double.compare(rating1, rating2) : Double.compare(rating2, rating1);
        }).collect(Collectors.toList());
    }

    private static List<Book> sortBooksByReviewCount(List<Book> books, String sortOrder) {
        return books.stream().sorted((book1, book2) -> {
            int count1 = ReviewDatabase.getInstance().getReviewsByBook(book1).size();
            int count2 = ReviewDatabase.getInstance().getReviewsByBook(book2).size();
            return sortOrder.equalsIgnoreCase("asc") ? Integer.compare(count1, count2) : Integer.compare(count2, count1);
        }).collect(Collectors.toList());
    }

    private static List<Map<String, Object>> formatBooks(List<Book> books) {
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
            formattedBook.put("averageRating", ReviewDatabase.getInstance().calculateAverageRating(book));
            formattedBooks.add(formattedBook);
        }
        return formattedBooks;
    }

    private static String buildSearchString(String title, String authorName, String genre, Integer fromYear, Integer toYear, String sortBy, String sortOrder) {
        StringBuilder searchString = new StringBuilder();
        if (title != null) searchString.append("title:").append(title).append(", ");
        if (authorName != null) searchString.append("author:").append(authorName).append(", ");
        if (genre != null) searchString.append("genre:").append(genre).append(", ");
        if (fromYear != null && toYear != null) searchString.append("year:").append(fromYear).append("-").append(toYear).append(", ");
        if (sortBy != null) searchString.append("sortBy:").append(sortBy).append(", ");
        if (sortOrder != null) searchString.append("sortOrder:").append(sortOrder).append(", ");
        if (searchString.length() > 2) searchString.setLength(searchString.length() - 2); // Remove trailing comma and space
        return searchString.toString();
    }
}
package ir.ac.ut.ece.ie.miobook.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import ir.ac.ut.ece.ie.miobook.database.UserDatabase;
import ir.ac.ut.ece.ie.miobook.model.Book;
import ir.ac.ut.ece.ie.miobook.model.BorrowedBook;
import ir.ac.ut.ece.ie.miobook.model.Role;
import ir.ac.ut.ece.ie.miobook.model.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShowPurchasedBooksService {

    public static String showPurchasedBooks(String username) throws IOException {
        User user = UserDatabase.getInstance().findUserByUsername(username);

        if (user == null) {
            throw new IllegalArgumentException("User not found.");
        }

        if (user.getRole() == Role.admin) {
            throw new IllegalArgumentException("Admins do not have purchased books.");
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
        for (BorrowedBook borrowedBook : user.getBorrowedBooks()) {
            if (borrowedBook.canAccessBook()) {
                Book book = borrowedBook.getBook();
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

        return new ObjectMapper().writeValueAsString(response);
    }
}
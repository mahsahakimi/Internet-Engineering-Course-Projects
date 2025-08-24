package ir.ac.ut.ece.ie.miobook.service;

import ir.ac.ut.ece.ie.miobook.database.AuthorDatabase;
import ir.ac.ut.ece.ie.miobook.database.BookDatabase;
import ir.ac.ut.ece.ie.miobook.database.UserDatabase;
import ir.ac.ut.ece.ie.miobook.model.Author;
import ir.ac.ut.ece.ie.miobook.model.Book;
import ir.ac.ut.ece.ie.miobook.model.Role;
import ir.ac.ut.ece.ie.miobook.model.User;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

public class AddBookService {

    public static void addBook(String username, String title, String authorName, String publisher, int year, Object price, List<String> genres, String synopsis, String content) {
        User user = UserDatabase.getInstance().findUserByUsername(username);

        if (user == null || user.getRole() != Role.admin) {
            throw new IllegalArgumentException("Permission denied.");
        }

        Author author = AuthorDatabase.getInstance().findAuthorByName(authorName);

        if (author == null) {
            throw new IllegalArgumentException("Author not found.");
        }

        if (year <= 0) {
            throw new IllegalArgumentException("Year must be a positive number.");
        }

        // Check if the author was alive in the book's year
        if (author.getBorn() != null && author.getBorn().getYear() > year) {
            throw new IllegalArgumentException("Author was not born yet in that year.");
        }
//        if (author.getDied() != null && author.getDied().getYear() < year) {
//            throw new IllegalArgumentException("Author was dead in that year.");
//        }

        if (genres == null || genres.isEmpty()) {
            throw new IllegalArgumentException("Book must have at least one genre.");
        }

        int finalPrice;

        JsonNode priceNode = (JsonNode) price;
        if (priceNode.isInt()) {
            finalPrice = priceNode.intValue();
            System.out.println("int: " + finalPrice);
        } else if (priceNode.isDouble() || priceNode.isFloat()) {
            double priceValue = priceNode.doubleValue();
            finalPrice = (int)(priceValue*100);
            System.out.println("double: " + priceValue);
        } else {
            throw new IllegalArgumentException("Price must be a number.");
        }

        Book book = new Book(title, author, publisher, year, genres, finalPrice, synopsis, content);
        BookDatabase.getInstance().addBook(book);
    }
}

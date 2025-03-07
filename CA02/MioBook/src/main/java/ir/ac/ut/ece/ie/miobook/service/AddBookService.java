package ir.ac.ut.ece.ie.miobook.service;

import ir.ac.ut.ece.ie.miobook.database.AuthorDatabase;
import ir.ac.ut.ece.ie.miobook.database.BookDatabase;
import ir.ac.ut.ece.ie.miobook.database.UserDatabase;
import ir.ac.ut.ece.ie.miobook.model.Author;
import ir.ac.ut.ece.ie.miobook.model.Book;
import ir.ac.ut.ece.ie.miobook.model.Role;
import ir.ac.ut.ece.ie.miobook.model.User;
import java.util.List;

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

        if (genres == null || genres.isEmpty()) {
            throw new IllegalArgumentException("Book must have at least one genre.");
        }

        int finalPrice;
        if (price instanceof Double) {
            finalPrice = (int) Math.round((Double) price * 100);
        } else if (price instanceof Integer) {
            finalPrice = (Integer) price;
        } else {
            throw new IllegalArgumentException("Price must be a number.");
        }

        Book book = new Book(title, author, publisher, year, genres, finalPrice, synopsis, content);
        BookDatabase.getInstance().addBook(book);
    }
}

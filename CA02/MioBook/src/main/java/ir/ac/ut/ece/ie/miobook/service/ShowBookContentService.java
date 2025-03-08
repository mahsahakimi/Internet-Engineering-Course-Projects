package ir.ac.ut.ece.ie.miobook.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import ir.ac.ut.ece.ie.miobook.database.BookDatabase;
import ir.ac.ut.ece.ie.miobook.database.UserDatabase;
import ir.ac.ut.ece.ie.miobook.model.Book;
import ir.ac.ut.ece.ie.miobook.model.BorrowedBook;
import ir.ac.ut.ece.ie.miobook.model.Role;
import ir.ac.ut.ece.ie.miobook.model.User;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShowBookContentService {

    public static String showBookContent(String username, String title) throws IOException {
        User user = UserDatabase.getInstance().findUserByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("User does not exist!");
        }
        if (user.getRole() == Role.admin) {
            throw new IllegalArgumentException("You are admin!");
        }
        Book book = BookDatabase.getInstance().findBookByTitle(title);
        if (book == null) {
            throw new IllegalArgumentException("Book does not exist!");
        }

        boolean hasAccess = false;
        // search in owned books
        List<Book> ownedBooks = user.getOwnedBooks();
        for (Book b : ownedBooks) {
            if (b.getTitle().equals(title)) {
                hasAccess = true;
                break;
            }
        }
        // search in borrowed books
        if (!hasAccess) {
            List<BorrowedBook> borrowedBooks = user.getBorrowedBooks();
            for (BorrowedBook borrowedBook : borrowedBooks) {
                if (borrowedBook.getBook().getTitle().equals(title) && borrowedBook.canAccessBook()) {
                    hasAccess = true;
                    break;
                }
            }
        }

        if (hasAccess) {
            Map<String, Object> data = new HashMap<>();
            data.put("title", book.getTitle());
            data.put("content", book.getContent());

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Book content retrieved successfully");
            response.put("data", data);

            return new ObjectMapper().writeValueAsString(response);
        }
        throw new IllegalArgumentException("User does not have access to the book!");
    }
}
package ir.ac.ut.ece.ie.miobook.service;

import ir.ac.ut.ece.ie.miobook.database.BookDatabase;
import ir.ac.ut.ece.ie.miobook.database.UserDatabase;
import ir.ac.ut.ece.ie.miobook.model.Book;
import ir.ac.ut.ece.ie.miobook.model.CartElement;
import ir.ac.ut.ece.ie.miobook.model.Role;
import ir.ac.ut.ece.ie.miobook.model.User;

public class RemoveCartService {

    public static void removeFromCart(String username, String title) {
        User user = UserDatabase.getInstance().findUserByUsername(username);
        Book book = BookDatabase.getInstance().findBookByTitle(title);

        if (user == null) {
            throw new IllegalArgumentException("User not found.");
        }

        if (book == null) {
            throw new IllegalArgumentException("Book not found.");
        }

        if (user.getRole() == Role.admin) {
            throw new IllegalArgumentException("Permission denied. Admin cannot remove from cart.");
        }

        boolean found = false;
        for (CartElement ce : user.getCart()) {
            if (ce.getBook().getTitle().equals(title)) {
                found = true;
                break;
            }
        }
        if (!found) {
            throw new IllegalArgumentException("Book not in cart.");
        }

        user.removeBook(book);
    }
}
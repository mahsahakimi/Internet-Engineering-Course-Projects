package ir.ac.ut.ece.ie.miobook.service;

import ir.ac.ut.ece.ie.miobook.database.BookDatabase;
import ir.ac.ut.ece.ie.miobook.database.UserDatabase;
import ir.ac.ut.ece.ie.miobook.model.Book;
import ir.ac.ut.ece.ie.miobook.model.CartElement;
import ir.ac.ut.ece.ie.miobook.model.Role;
import ir.ac.ut.ece.ie.miobook.model.User;

public class AddCartService {

    public static void addToCart(String username, String title) {
        User user = UserDatabase.getInstance().findUserByUsername(username);
        Book book = BookDatabase.getInstance().findBookByTitle(title);

        if (user == null) {
            throw new IllegalArgumentException("User not found.");
        }

        if (book == null) {
            throw new IllegalArgumentException("Book not found.");
        }

        if (user.getRole() == Role.admin) {
            throw new IllegalArgumentException("Permission denied. Admin cannot add to cart.");
        }

        if (user.getCart().size() >= 10) {
            throw new IllegalArgumentException("Cart is full.");
        }

//        check if available in cart

        CartElement newElement = new CartElement(book, 0);
        user.getCart().add(newElement);
    }
}
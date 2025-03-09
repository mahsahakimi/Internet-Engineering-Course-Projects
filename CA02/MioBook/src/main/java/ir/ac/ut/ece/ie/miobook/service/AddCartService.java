package ir.ac.ut.ece.ie.miobook.service;

import ir.ac.ut.ece.ie.miobook.database.BookDatabase;
import ir.ac.ut.ece.ie.miobook.database.UserDatabase;
import ir.ac.ut.ece.ie.miobook.model.*;

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

        // Check if the book is already in the cart
        if (user.getCart().stream().anyMatch(cartElement -> cartElement.getBook().equals(book))) {
            throw new IllegalArgumentException("Book is already in the cart.");
        }

        // Check if the book is already owned
        if (user.getOwnedBooks().stream().anyMatch(ownedBook -> ownedBook.equals(book))) {
            throw new IllegalArgumentException("Book is already owned.");
        }

        // Check if the book is already borrowed
        if (user.getBorrowedBooks().stream().anyMatch(borrowedBook -> borrowedBook.getBook().equals(book) && borrowedBook.canAccessBook())) {
            throw new IllegalArgumentException("Book is already borrowed and not expired.");
        }

        CartElement newElement = new CartElement(book, 0);
        user.getCart().add(newElement);
    }
}
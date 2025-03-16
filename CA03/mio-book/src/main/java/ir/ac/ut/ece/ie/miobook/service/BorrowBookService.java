package ir.ac.ut.ece.ie.miobook.service;

import ir.ac.ut.ece.ie.miobook.database.BookDatabase;
import ir.ac.ut.ece.ie.miobook.database.UserDatabase;
import ir.ac.ut.ece.ie.miobook.model.Book;
import ir.ac.ut.ece.ie.miobook.model.CartElement;
import ir.ac.ut.ece.ie.miobook.model.Role;
import ir.ac.ut.ece.ie.miobook.model.User;

public class BorrowBookService {

    public static void borrowBook(String username, String title, int days) {
        User user = UserDatabase.getInstance().findUserByUsername(username);
        Book book = BookDatabase.getInstance().findBookByTitle(title);

        if (user == null) {
            throw new IllegalArgumentException("User not found.");
        }

        if (user.getRole() != Role.customer) {
            throw new IllegalArgumentException("Only customers can borrow books.");
        }

        if (book == null) {
            throw new IllegalArgumentException("Book not found.");
        }

        if (days < 1 || days > 9) {
            throw new IllegalArgumentException("Invalid number of days. Days must be between 1 and 9.");
        }

        // Check if the book is already in the cart
        if (user.getCart().stream().anyMatch(cartElement -> cartElement.getBook().equals(book))) {
            throw new IllegalArgumentException("Book is already in the cart.");
        }

        // Check if the book is already owned
        if (user.getOwnedBooks().stream().anyMatch(ownedBook -> ownedBook.equals(book))) {
            throw new IllegalArgumentException("Book is already owned.");
        }

        // Check if the book is already borrowed and not expired
        if (user.getBorrowedBooks().stream().anyMatch(borrowedBook -> borrowedBook.getBook().equals(book) && borrowedBook.canAccessBook())) {
            throw new IllegalArgumentException("Book is already borrowed and not expired.");
        }

        CartElement newElement = new CartElement(book, days);
        user.getCart().add(newElement);
    }
}
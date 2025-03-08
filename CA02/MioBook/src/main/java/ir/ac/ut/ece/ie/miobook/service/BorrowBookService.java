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

//        check if available in cart

        CartElement newElement = new CartElement(book, days);
        user.addToCart(newElement);
    }
}
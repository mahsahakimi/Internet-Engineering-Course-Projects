package ir.ac.ut.ece.ie.miobook.service;

import ir.ac.ut.ece.ie.miobook.database.BookDatabase;
import ir.ac.ut.ece.ie.miobook.database.ReviewDatabase;
import ir.ac.ut.ece.ie.miobook.database.UserDatabase;
import ir.ac.ut.ece.ie.miobook.model.Book;
import ir.ac.ut.ece.ie.miobook.model.Review;
import ir.ac.ut.ece.ie.miobook.model.Role;
import ir.ac.ut.ece.ie.miobook.model.User;

import java.time.LocalDate;


public class AddReviewService {

    public static void addReview(String username, String title, int rate, String comment) {
        User user = UserDatabase.getInstance().findUserByUsername(username);
        Book book = BookDatabase.getInstance().findBookByTitle(title);

        if (user == null) {
            throw new IllegalArgumentException("User not found.");
        }

        if (book == null) {
            throw new IllegalArgumentException("Book not found.");
        }

        if (user.getRole() == Role.admin) {
            throw new IllegalArgumentException("Permission denied.");
        }

        if (!user.getOwnedBooks().contains(book) && user.getBorrowedBooks().stream().noneMatch(borrowedBook -> borrowedBook.getBook().equals(book) && borrowedBook.canAccessBook())) {
            throw new IllegalArgumentException("You must purchase or borrow the book to add a review.");
        }

        if (rate < 1 || rate > 5) {
            throw new IllegalArgumentException("Rate must be an integer between 1 and 5.");
        }

        LocalDate submissionTime = LocalDate.now();
        Review review = new Review(username, title, rate, comment, submissionTime);
        ReviewDatabase.getInstance().addReview(review);
    }
}

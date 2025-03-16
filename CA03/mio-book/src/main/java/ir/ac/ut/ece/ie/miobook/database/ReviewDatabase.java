package ir.ac.ut.ece.ie.miobook.database;

import ir.ac.ut.ece.ie.miobook.model.Book;
import ir.ac.ut.ece.ie.miobook.model.Review;
import ir.ac.ut.ece.ie.miobook.model.User;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ReviewDatabase {

    private static ReviewDatabase instance;
    private List<Review> reviews = new ArrayList<>();

    private ReviewDatabase() {}

    public static ReviewDatabase getInstance() {
        if (instance == null) {
            instance = new ReviewDatabase();
        }
        return instance;
    }
    public List<Review> getReviews(){
        return reviews;
    }

    public void addReview(Review review) {
        reviews.removeIf(r -> r.getUsername().equals(review.getUsername()) && r.getTitle().equals(review.getTitle()));
        reviews.add((review));
    }

    public List<Review> getReviewsByBook(Book book) {
        return reviews.stream()
                .filter(review -> {
                    Book reviewBook = BookDatabase.getInstance().findBookByTitle(review.getTitle());
                    return reviewBook != null && reviewBook.equals(book);
                }).collect(Collectors.toList());
    }

    public double calculateAverageRating(Book book) {
        List<Review> bookReviews = getReviewsByBook(book);
        if (bookReviews.isEmpty()) {
            return 0; // No reviews, so average is 0
        }

        double totalRating = bookReviews.stream().mapToInt(Review::getRate).sum();
        return totalRating / bookReviews.size();
    }
}

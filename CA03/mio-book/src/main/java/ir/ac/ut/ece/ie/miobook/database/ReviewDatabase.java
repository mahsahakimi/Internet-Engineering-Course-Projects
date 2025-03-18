package ir.ac.ut.ece.ie.miobook.database;

import ir.ac.ut.ece.ie.miobook.model.Book;
import ir.ac.ut.ece.ie.miobook.model.Review;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Repository
public class ReviewDatabase {

    private List<Review> reviews = new ArrayList<>();
    private final BookDatabase bookDatabase;

    public ReviewDatabase(BookDatabase bookDatabase) {
        this.bookDatabase = bookDatabase;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void addReview(Review review) {
        reviews.removeIf(r -> r.getUsername().equals(review.getUsername()) && r.getTitle().equals(review.getTitle()));
        reviews.add((review));
    }

    public List<Review> getReviewsByBook(Book book) {
        return reviews.stream()
                .filter(review -> {
                    Book reviewBook = bookDatabase.findBookByTitle(review.getTitle());
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
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
        String name = review.getUsername();
        String title = review.getTitle();

        Book book = BookDatabase.getInstance().findBookByTitle(title);
        if (book == null){
            throw new IllegalArgumentException("Book does not exist : " + title);
        }
        User user = UserDatabase.getInstance().findUserByUsername(name);
        if (user == null){
            throw new IllegalArgumentException("User does not exist : " + name);
        }
        int rate = review.getRate();
        if(rate < 1 || rate > 5 ){
            throw new IllegalArgumentException("Invalid value for rating : " + rate);
        }
        reviews.add((review));
    }

    public List<Review> getReviewsByBook(Book book) {
        return reviews.stream()
                .filter(review -> {
                    Book reviewBook = BookDatabase.getInstance().findBookByTitle(review.getTitle());
                    return reviewBook != null && reviewBook.equals(book);
                }).collect(Collectors.toList());
    }
}

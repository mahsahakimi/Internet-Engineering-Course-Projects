package ir.ac.ut.ece.ie.miobook.repository;

import ir.ac.ut.ece.ie.miobook.model.Book;
import ir.ac.ut.ece.ie.miobook.model.Customer;
import ir.ac.ut.ece.ie.miobook.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByBook(Book book);

    List<Review> findByCustomer(Customer customer);

    @Query("SELECT AVG(r.rate) FROM Review r WHERE r.book = :book")
    Double calculateAverageRatingByBook(@Param("book") Book book);

    boolean existsByCustomerAndBook(Customer customer, Book book);
}
package ir.ac.ut.ece.ie.miobook.repository;

import ir.ac.ut.ece.ie.miobook.model.Book;
import ir.ac.ut.ece.ie.miobook.model.Customer;
import ir.ac.ut.ece.ie.miobook.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByBook(Book book);

    long countByBook(Book book);

    List<Review> findByCustomer(Customer Customer);

    Page<Review> findByBook(Book book, Pageable pageable);

    @Query("SELECT AVG(r.rate) FROM Review r WHERE r.book = :book")
    Double calculateAverageRatingByBook(@Param("book") Book book);

    boolean existsByCustomerAndBook(Customer customer, Book book);
}

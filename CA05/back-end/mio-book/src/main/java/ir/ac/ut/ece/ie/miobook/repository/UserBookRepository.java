package ir.ac.ut.ece.ie.miobook.repository;

import ir.ac.ut.ece.ie.miobook.model.Book;
import ir.ac.ut.ece.ie.miobook.model.Customer;
import ir.ac.ut.ece.ie.miobook.model.MyLocalDateTime;
import ir.ac.ut.ece.ie.miobook.model.UserBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserBookRepository extends JpaRepository<UserBook, Long> {

    // Find by customer and book
    Optional<UserBook> findByCustomerAndBook(Customer customer, Book book);

    // Find all books owned by a customer (both purchased and borrowed)
    List<UserBook> findByCustomer(Customer customer);

    // Find all books purchased by a customer (not borrowed)
    @Query("SELECT ub FROM UserBook ub WHERE ub.customer = :customer AND ub.isBorrowed = false")
    List<UserBook> findOwnedBooksByCustomer(@Param("customer") Customer customer);

    // Find all books currently borrowed by a customer
    @Query("SELECT ub FROM UserBook ub WHERE ub.customer = :customer AND ub.isBorrowed = true")
    List<UserBook> findBorrowedBooksByCustomer(@Param("customer") Customer customer);

    // Find all books a customer can currently access
    // (purchased books + borrowed books that aren't expired)
    @Query("SELECT ub FROM UserBook ub WHERE ub.customer = :customer AND " +
            "(ub.isBorrowed = false OR " +
            "(ub.isBorrowed = true AND ub.borrowStartDate.plusDays(ub.borrowDays) > CURRENT_TIMESTAMP))")
    List<UserBook> findAccessibleBooksByCustomer(@Param("customer") Customer customer);

    // Find all books with expired borrow time for a customer
    @Query("SELECT ub FROM UserBook ub WHERE ub.customer = :customer AND " +
            "ub.isBorrowed = true AND " +
            "ub.borrowStartDate.plusDays(ub.borrowDays) <= CURRENT_TIMESTAMP")
    List<UserBook> findExpiredBorrowedBooksByCustomer(@Param("customer") Customer customer);

    // Check if a customer owns a specific book
    boolean existsByCustomerAndBookAndIsBorrowedFalse(Customer customer, Book book);

    // Check if a customer has borrowed a specific book
    boolean existsByCustomerAndBookAndIsBorrowedTrue(Customer customer, Book book);

    // Check if a book is currently borrowed by any customer
    boolean existsByBookAndIsBorrowedTrue(Book book);

    // Update borrow status
    @Modifying
    @Transactional
    @Query("UPDATE UserBook ub SET " +
            "ub.isBorrowed = :isBorrowed, " +
            "ub.borrowDays = :borrowDays, " +
            "ub.borrowStartDate = :startDate " +
            "WHERE ub.id = :id")
    int updateBorrowStatus(
            @Param("id") Long id,
            @Param("isBorrowed") boolean isBorrowed,
            @Param("borrowDays") Integer borrowDays,
            @Param("startDate") MyLocalDateTime startDate
    );

    // Find all overdue books in the system
    @Query("SELECT ub FROM UserBook ub WHERE " +
            "ub.isBorrowed = true AND " +
            "ub.borrowStartDate.plusDays(ub.borrowDays) < CURRENT_TIMESTAMP")
    List<UserBook> findAllOverdueBooks();

    // Count how many customers own a specific book
    long countByBook(Book book);

    // Count how many customers have borrowed a specific book
    long countByBookAndIsBorrowedTrue(Book book);
}
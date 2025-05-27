package ir.ac.ut.ece.ie.miobook.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "UserBook")
@Table(name = "user_book")
@Getter
@Setter
@NoArgsConstructor
public class UserBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_username", nullable = false,
            foreignKey = @ForeignKey(name = "fk_userbook_user"))
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_title", nullable = false,
            foreignKey = @ForeignKey(name = "fk_userbook_book"))
    private Book book;

    @Column(nullable = false)
    private boolean isBorrowed = false;

    @Column(name = "borrow_days")
    private Integer borrowDays;

    @Column(name = "borrow_start_date")
    private MyLocalDateTime borrowStartDate;

    public UserBook(Customer customer, Book book) {
        this.customer = customer;
        this.book = book;
    }

    public void markAsBorrowed(int borrowDays, MyLocalDateTime startDate) {
        this.isBorrowed = true;
        this.borrowDays = borrowDays;
        this.borrowStartDate = startDate;
    }

    public boolean canAccessBook() {
        if (!isBorrowed || borrowStartDate == null || borrowDays == null) {
            return true;
        }
        MyLocalDateTime dueDate = borrowStartDate.plusDays(borrowDays);
        return MyLocalDateTime.now().isBefore(dueDate);
    }

    public MyLocalDateTime getDueDate() {
        return borrowStartDate.plusDays(borrowDays);
    }
}
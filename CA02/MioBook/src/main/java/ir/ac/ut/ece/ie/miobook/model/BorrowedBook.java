package ir.ac.ut.ece.ie.miobook.model;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BorrowedBook {

    private Book book;
    private LocalDateTime dueDate;
    private double borrowPrice;

    public BorrowedBook(Book book, LocalDateTime dueDate, double borrowPrice) {
        this.book = book;
        this.dueDate = dueDate;
        this.borrowPrice = borrowPrice;
    }
}

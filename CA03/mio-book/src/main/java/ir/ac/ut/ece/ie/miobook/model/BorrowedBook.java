package ir.ac.ut.ece.ie.miobook.model;

import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class BorrowedBook {
    private Book book;
    private LocalDateTime dueDate;

    public BorrowedBook(Book book, LocalDateTime dueDate) {
        this.book = book;
        this.dueDate = dueDate;
    }

    public boolean canAccessBook() {
        LocalDateTime now = LocalDateTime.now();
        return now.isBefore(dueDate);
    }
}
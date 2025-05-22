package ir.ac.ut.ece.ie.miobook.model;

import lombok.Getter;

@Getter
public class BorrowedBook {
    private Book book;
    private MyLocalDateTime dueDate;

    public BorrowedBook(Book book, MyLocalDateTime dueDate) {
        this.book = book;
        this.dueDate = dueDate;
    }

    public boolean canAccessBook() {
        MyLocalDateTime now = MyLocalDateTime.now();
        return now.isBefore(dueDate);
    }
}
package ir.ac.ut.ece.ie.miobook.database;

import ir.ac.ut.ece.ie.miobook.model.Book;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookDatabase {

    private static BookDatabase instance;
    private List<Book> books = new ArrayList<>();

    public List<Book> getBooks() {
        return books;
    }

    private BookDatabase() {}

    public static BookDatabase getInstance() {
        if (instance == null) {
            instance = new BookDatabase();
        }
        return instance;
    }

    public void addBook(Book book) {
        if (findBookByTitle(book.getTitle()) != null) {
            throw new IllegalArgumentException("Book title already exists: " + book.getTitle());
        }
        books.add(book);
    }

    public Book findBookByTitle(String title) {
        Optional<Book> foundBook = books.stream()
                .filter(book -> book.getTitle().equals(title))
                .findFirst();

        return foundBook.orElse(null);
    }
}

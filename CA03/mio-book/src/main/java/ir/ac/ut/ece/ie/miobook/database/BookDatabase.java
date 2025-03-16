package ir.ac.ut.ece.ie.miobook.database;

import ir.ac.ut.ece.ie.miobook.model.Book;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public List<Book> findBooksByTitle(String title) {
        return books.stream()
                .filter(book -> book.getTitle().toLowerCase().contains(title.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Book> findBooksByAuthor(String authorName) {
        return books.stream()
                .filter(book -> book.getAuthor().getName().toLowerCase().contains(authorName.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Book> findBooksByGenre(String genre) {
        return books.stream()
                .filter(book -> book.getGenres().stream().anyMatch(g -> g.equalsIgnoreCase(genre)))
                .collect(Collectors.toList());
    }

    public List<Book> findBooksByYear(int fromYear, int toYear) {
        return books.stream()
                .filter(book -> book.getYear() >= fromYear && book.getYear() <= toYear)
                .collect(Collectors.toList());
    }
}

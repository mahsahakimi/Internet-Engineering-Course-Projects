package ir.ac.ut.ece.ie.miobook.database;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AuthorDatabase {

    private static AuthorDatabase instance;
    private List<String> authors = new ArrayList<>();

    private AuthorDatabase() {}

    public static AuthorDatabase getInstance() {
        if (instance == null) {
            instance = new AuthorDatabase();
        }
        return instance;
    }

    public void addAuthor(String author) {
        if (findAuthorByName(author) == null) {
            authors.add(author);
        } else {
            throw new IllegalArgumentException("Author already exists: " + author);
        }
    }

    public String findAuthorByName(String author) {
        Optional<String> foundAuthor = authors.stream()
                .filter(a -> a.equals(author))
                .findFirst();

        return foundAuthor.orElse(null);
    }

    public List<String> getAuthors() {
        return authors;
    }
}

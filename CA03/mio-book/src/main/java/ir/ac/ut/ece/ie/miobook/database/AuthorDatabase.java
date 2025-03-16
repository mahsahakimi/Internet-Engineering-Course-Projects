package ir.ac.ut.ece.ie.miobook.database;

import ir.ac.ut.ece.ie.miobook.model.Author;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class AuthorDatabase {

    private static AuthorDatabase instance;
    private List<Author> authors = new ArrayList<>();

    private AuthorDatabase() {}

    public static AuthorDatabase getInstance() {
        if (instance == null) {
            instance = new AuthorDatabase();
        }
        return instance;
    }

    public void addAuthor(Author author) {
        String name = author.getName();
        if (findAuthorByName(name) == null) {
            authors.add(author);
        } else {
            throw new IllegalArgumentException("Author already exists: " + author);
        }
    }

    public Author findAuthorByName(String name) {
        for (Author author : authors) {
            if(author.getName().equals(name)) {
                return author;
            }
        }
        return null;
    }
}

package ir.ac.ut.ece.ie.miobook.database;

import ir.ac.ut.ece.ie.miobook.exceptions.AuthorAlreadyExistsException;
import ir.ac.ut.ece.ie.miobook.model.Author;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class AuthorDatabase {

    private List<Author> authors = new ArrayList<>();

    public List<Author> getAuthors() {
        return authors;
    }

    public void addAuthor(Author author) {
        String name = author.getName();
        if (findAuthorByName(name) == null) {
            authors.add(author);
        } else {
            throw new AuthorAlreadyExistsException(author.getName());
        }
    }

    public Author findAuthorByName(String name) {
        for (Author author : authors) {
            if (author.getName().equals(name)) {
                return author;
            }
        }
        return null;
    }
}

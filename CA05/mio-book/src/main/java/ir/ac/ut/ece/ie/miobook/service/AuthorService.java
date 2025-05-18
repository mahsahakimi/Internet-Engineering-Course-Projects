package ir.ac.ut.ece.ie.miobook.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import ir.ac.ut.ece.ie.miobook.database.AuthorDatabase;
import ir.ac.ut.ece.ie.miobook.database.UserDatabase;
import ir.ac.ut.ece.ie.miobook.exceptions.*;
import ir.ac.ut.ece.ie.miobook.model.Author;
import ir.ac.ut.ece.ie.miobook.model.Book;
import ir.ac.ut.ece.ie.miobook.model.MyLocalDate;
import ir.ac.ut.ece.ie.miobook.model.Role;
import ir.ac.ut.ece.ie.miobook.model.User;
import ir.ac.ut.ece.ie.miobook.utils.PaginationUtils;
import ir.ac.ut.ece.ie.miobook.validator.BornDiedValidator;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AuthorService {

    private final AuthorDatabase authorDatabase;
    private final UserDatabase userDatabase;

    public AuthorService(AuthorDatabase authorDatabase, UserDatabase userDatabase) {
        this.authorDatabase = authorDatabase;
        this.userDatabase = userDatabase;
    }

    public List<Author> addAuthor(String name, String penName, String nationality, MyLocalDate born, MyLocalDate died) {
        User whoAdded = userDatabase.getLoggedInUser();
        if (whoAdded == null) {
            throw new UserNotLoggedInException();
        }
        // if (whoAdded.getRole() == Role.CUSTOMER) {
        //     throw new PermissionDeniedException(Role.CUSTOMER.toString());
        // }

        if (!BornDiedValidator.validateBornDied(born.toLocalDate(), died != null ? died.toLocalDate() : null)) {
            throw new InvalidDatesException();
        }

        Author author = new Author(name, penName, nationality, born, died);
        authorDatabase.addAuthor(author);
        return authorDatabase.getAuthors();
    }

    public String getAuthorDetails(String authorName) {
        Author author = authorDatabase.findAuthorByName(authorName);

        if (author == null) {
            throw new AuthorNotFoundException(authorName);
        }

        Map<String, Object> authorData = new HashMap<>();
        authorData.put("name", author.getName());
        authorData.put("penName", author.getPenName());
        authorData.put("nationality", author.getNationality());
        authorData.put("born", author.getBorn().toString());

        // Only add "died" if it's not null
        if (author.getDied() != null) {
            authorData.put("died", author.getDied().toString());
        }

        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(authorData);
        } catch (IOException e) {
            throw new RuntimeException("Could not convert author details to JSON.", e);
        }
    }

    public List<Author> getAllAuthors(int page, int pageSize){
        List<Author> authors =  authorDatabase.getAuthors();
        return PaginationUtils.paginate(authors, page, pageSize);
    }
}

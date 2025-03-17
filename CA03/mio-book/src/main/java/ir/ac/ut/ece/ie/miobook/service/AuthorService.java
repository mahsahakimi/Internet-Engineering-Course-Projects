package ir.ac.ut.ece.ie.miobook.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import ir.ac.ut.ece.ie.miobook.database.AuthorDatabase;
import ir.ac.ut.ece.ie.miobook.database.UserDatabase;
import ir.ac.ut.ece.ie.miobook.exceptions.AuthorNotFoundException;
import ir.ac.ut.ece.ie.miobook.exceptions.InvalidDatesException;
import ir.ac.ut.ece.ie.miobook.exceptions.InvalidRoleException;
import ir.ac.ut.ece.ie.miobook.exceptions.UserNotFoundException;
import ir.ac.ut.ece.ie.miobook.model.Author;
import ir.ac.ut.ece.ie.miobook.model.MySimpleDate;
import ir.ac.ut.ece.ie.miobook.model.Role;
import ir.ac.ut.ece.ie.miobook.model.User;
import ir.ac.ut.ece.ie.miobook.validator.BornDiedValidator;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthorService {

    public void addAuthor(String username, String name, String penName, String nationality, MySimpleDate born, MySimpleDate died) {
        User whoAdded = UserDatabase.getInstance().findUserByUsername(username);

        if (whoAdded == null) {
            throw new UserNotFoundException(username);
        } else if (whoAdded.getRole() == Role.customer) {
            throw new InvalidRoleException(Role.customer.toString());
        }

        if (!BornDiedValidator.validateBornDied(born.toLocalDate(), died.toLocalDate())) {
            throw new InvalidDatesException();
        }

        Author author = new Author(name, penName, nationality, born, died);
        AuthorDatabase.getInstance().addAuthor(author);
    }

    public String getAuthorDetails(String authorName) {
        Author author = AuthorDatabase.getInstance().findAuthorByName(authorName);

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
}

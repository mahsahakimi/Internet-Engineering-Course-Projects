package ir.ac.ut.ece.ie.miobook.service;

import ir.ac.ut.ece.ie.miobook.database.AuthorDatabase;
import ir.ac.ut.ece.ie.miobook.database.UserDatabase;
import ir.ac.ut.ece.ie.miobook.model.Author;
import ir.ac.ut.ece.ie.miobook.model.MySimpleDate;
import ir.ac.ut.ece.ie.miobook.model.Role;
import ir.ac.ut.ece.ie.miobook.model.User;
import ir.ac.ut.ece.ie.miobook.validator.BornDiedValidator;

//deprecated!
public class AddAuthorService {

    public static void addAuthor(String username, String name, String penName, String nationality, MySimpleDate born, MySimpleDate died) {
        User whoAdded = UserDatabase.getInstance().findUserByUsername(username);
        if (whoAdded == null) {
            throw new IllegalArgumentException("There is no user with this username!");
        } else if (whoAdded.getRole() == Role.customer) {
            throw new IllegalArgumentException("The user is not an admin!");
        }

        if (!BornDiedValidator.validateBornDied(born.toLocalDate(), died.toLocalDate())) {
            throw new IllegalArgumentException("Invalid dates!");
        }

        Author author = new Author(name, penName, nationality, born, died);
        AuthorDatabase.getInstance().addAuthor(author);
    }
}

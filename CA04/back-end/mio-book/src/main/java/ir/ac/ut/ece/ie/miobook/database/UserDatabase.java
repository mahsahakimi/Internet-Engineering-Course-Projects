package ir.ac.ut.ece.ie.miobook.database;

import ir.ac.ut.ece.ie.miobook.exceptions.EmailAlreadyExistsException;
import ir.ac.ut.ece.ie.miobook.exceptions.UsernameAlreadyExistsException;
import ir.ac.ut.ece.ie.miobook.model.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class UserDatabase {

    private User loggedInUser = null;
    private final List<User> users = new ArrayList<>();

    public List<User> getUsers() {
        return users;
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(User loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    public void addUser(User user) {
        if (!isUsernameUnique(user.getUsername())) {
            throw new UsernameAlreadyExistsException(user.getUsername());
        }
        if (!isEmailUnique(user.getEmail())) {
            throw new EmailAlreadyExistsException(user.getEmail());
        }
        users.add(user);
    }

    public User findUserByUsername(String username) {
        Optional<User> foundUser = users.stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst();

        return foundUser.orElse(null);
    }

    private boolean isUsernameUnique(String username) {
        return users.stream()
                .noneMatch(user -> user.getUsername().equals(username));
    }

    private boolean isEmailUnique(String email) {
        return users.stream()
                .noneMatch(user -> user.getEmail().equals(email));
    }
}
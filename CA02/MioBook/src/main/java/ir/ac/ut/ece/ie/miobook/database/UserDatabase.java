package ir.ac.ut.ece.ie.miobook.database;

import ir.ac.ut.ece.ie.miobook.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserDatabase {

    private static UserDatabase instance;
    private List<User> users = new ArrayList<>();

    public List<User> getUsers() {
        return users;
    }

    private UserDatabase() {}

    public static UserDatabase getInstance() {
        if (instance == null) {
            instance = new UserDatabase();
        }
        return instance;
    }

    public void addUser(User user) {
        if (!isUsernameUnique(user.getUsername())) {
            throw new IllegalArgumentException("Username already exists: " + user.getUsername());
        }
        if (!isEmailUnique(user.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + user.getEmail());
        }
        users.add(user);
    }

    private boolean isUsernameUnique(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return false;
            }
        }
        return true;
    }

    private boolean isEmailUnique(String email) {
        for (User user : users) {
            if (user.getEmail().equals(email)) {
                return false;
            }
        }
        return true;
    }
}
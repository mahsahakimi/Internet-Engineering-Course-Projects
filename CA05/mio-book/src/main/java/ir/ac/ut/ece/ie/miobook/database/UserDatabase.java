package ir.ac.ut.ece.ie.miobook.database;

import ir.ac.ut.ece.ie.miobook.model.User;
import ir.ac.ut.ece.ie.miobook.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserDatabase {

    @Autowired
    private UserRepository userRepository;

    private String loggedInUsername = null;
    public User getLoggedInUser() {
        if (loggedInUsername != null) {
            return userRepository.findById(loggedInUsername).orElse(null);
        }
        return null;
    }
    public void setLoggedInUser(User loggedInUser) {
        this.loggedInUsername = loggedInUser.getUsername();
    }
}
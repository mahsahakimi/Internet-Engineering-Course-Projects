package ir.ac.ut.ece.ie.miobook.context;

import ir.ac.ut.ece.ie.miobook.model.User;
import ir.ac.ut.ece.ie.miobook.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserContext {

    private String loggedInUsername;

    private final UserRepository userRepository;

    @Autowired
    public UserContext(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void setLoggedInUser(User user) {
        this.loggedInUsername = user.getUsername();
    }

    public User getLoggedInUser() {
        if (loggedInUsername != null) {
            return userRepository.findByUsername(loggedInUsername).orElse(null);
        }
        return null;
    }

    public void clear() {
        this.loggedInUsername = null;
    }
}

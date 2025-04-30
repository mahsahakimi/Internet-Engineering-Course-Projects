package ir.ac.ut.ece.ie.miobook.service;

import ir.ac.ut.ece.ie.miobook.database.UserDatabase;
import ir.ac.ut.ece.ie.miobook.exceptions.IncorrectPasswordException;
import ir.ac.ut.ece.ie.miobook.exceptions.UserNotFoundException;
import ir.ac.ut.ece.ie.miobook.exceptions.UserNotLoggedInException;
import ir.ac.ut.ece.ie.miobook.exceptions.UserSessionActiveException;
import ir.ac.ut.ece.ie.miobook.model.User;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserDatabase userDatabase;

    public AuthService(UserDatabase userDatabase) {
        this.userDatabase = userDatabase;
    }

    public User userLogin(String username, String password){
        if(userDatabase.getLoggedInUser() != null) {
            throw new UserSessionActiveException();
        }
        User user = userDatabase.findUserByUsername(username);
        if (user == null) {
            throw new UserNotFoundException(username);
        }
        if (!user.getPassword().equals(password)){
            throw new IncorrectPasswordException();
        }
        userDatabase.setLoggedInUser(user);
        return user;
    }

    public void userLogout(){
        if(userDatabase.getLoggedInUser() == null){
            throw new UserNotLoggedInException();
        }
        userDatabase.setLoggedInUser(null);
    }
}

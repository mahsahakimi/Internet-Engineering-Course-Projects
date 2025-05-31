package ir.ac.ut.ece.ie.miobook.service;

import ir.ac.ut.ece.ie.miobook.context.UserContext;
import ir.ac.ut.ece.ie.miobook.exceptions.IncorrectPasswordException;
import ir.ac.ut.ece.ie.miobook.exceptions.UserNotFoundException;
import ir.ac.ut.ece.ie.miobook.exceptions.UserNotLoggedInException;
import ir.ac.ut.ece.ie.miobook.exceptions.UserSessionActiveException;
import ir.ac.ut.ece.ie.miobook.exceptions.WalletNotFoundException;
import ir.ac.ut.ece.ie.miobook.model.User;
import ir.ac.ut.ece.ie.miobook.model.Wallet;
import ir.ac.ut.ece.ie.miobook.repository.UserRepository;
import ir.ac.ut.ece.ie.miobook.repository.WalletRepository;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserContext userContext;
    private final UserRepository userRepository;
    private final WalletRepository walletRepository;


    public AuthService(UserContext userContext, UserRepository userRepository, WalletRepository walletRepository) {
        this.userContext = userContext;
        this.userRepository = userRepository;
        this.walletRepository = walletRepository;
    }

    public Map<String, Object> userLogin(String username, String password){
        if(userContext.getLoggedInUser() != null) {
            throw new UserSessionActiveException();
        }
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UserNotFoundException(username));
            
        if (!user.getPassword().equals(password)){
            throw new IncorrectPasswordException();
        }
        userContext.setLoggedInUser(user);
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("username", user.getUsername());
        userInfo.put("role", user.getRole());
        userInfo.put("email", user.getEmail());
        Wallet w = walletRepository.findByUsername(username)
            .orElseThrow(() -> new WalletNotFoundException(username));
        userInfo.put("balance", w.getBalance());
        return userInfo;
    }

    public void userLogout(){
        if(userContext.getLoggedInUser() == null){
            throw new UserNotLoggedInException();
        }
        userContext.clear();
    }
}

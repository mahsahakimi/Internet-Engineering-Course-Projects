package ir.ac.ut.ece.ie.miobook.service;

import ir.ac.ut.ece.ie.miobook.context.UserContext;
import ir.ac.ut.ece.ie.miobook.exceptions.IncorrectPasswordException;
import ir.ac.ut.ece.ie.miobook.exceptions.UserNotFoundException;
import ir.ac.ut.ece.ie.miobook.model.User;
import ir.ac.ut.ece.ie.miobook.repository.UserRepository;
import ir.ac.ut.ece.ie.miobook.utils.JwtUtil;
import ir.ac.ut.ece.ie.miobook.utils.PasswordHasher;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserContext userContext;
    private final UserRepository userRepository;

    public AuthService(UserContext userContext,
                       UserRepository userRepository ) {
        this.userContext = userContext;
        this.userRepository = userRepository;
    }

    public Map<String, Object> loginAndCreateSession(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        String expectedHash = PasswordHasher.hashPassword(password, user.getSalt());
        if (!expectedHash.equals(user.getPassword())) {
            throw new IncorrectPasswordException();
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put("username", user.getUsername());
        claims.put("email", user.getEmail());

        String token = JwtUtil.generateToken(user.getUsername(), claims);

        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("username", user.getUsername());
        userInfo.put("email", user.getEmail());
        userInfo.put("role", user.getRole());
        userInfo.put("token", token);

        return userInfo;
    }

    public void userLogout(String sessionToken) {
        userContext.clear(sessionToken);
    }
}
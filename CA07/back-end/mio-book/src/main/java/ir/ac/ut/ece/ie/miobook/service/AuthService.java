package ir.ac.ut.ece.ie.miobook.service;

import ir.ac.ut.ece.ie.miobook.context.UserContext;
import ir.ac.ut.ece.ie.miobook.exceptions.IncorrectPasswordException;
import ir.ac.ut.ece.ie.miobook.exceptions.UserNotFoundException;
import ir.ac.ut.ece.ie.miobook.model.User;
import ir.ac.ut.ece.ie.miobook.repository.UserRepository;
import ir.ac.ut.ece.ie.miobook.utils.PasswordHasher;

import org.springframework.data.redis.core.RedisTemplate;
import java.time.Duration;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserContext userContext;
    private final UserRepository userRepository;
    private final RedisTemplate<String, String> redisTemplate;

    public AuthService(UserContext userContext,
                       UserRepository userRepository,
                       RedisTemplate<String, String> redisTemplate) {
        this.userContext = userContext;
        this.userRepository = userRepository;
        this.redisTemplate = redisTemplate;
    }

    public Map<String, Object> loginAndCreateSession(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        String expectedHash = PasswordHasher.hashPassword(password, user.getSalt());
        if (!expectedHash.equals(user.getPassword())) {
                throw new IncorrectPasswordException();
        }

        String sessionId = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(sessionId, user.getUsername(), Duration.ofMinutes(30));

        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("username", user.getUsername());
        userInfo.put("role", user.getRole());
        userInfo.put("email", user.getEmail());
        userInfo.put("sessionId", sessionId);

        return userInfo;
    }

    public void userLogout(String sessionToken) {
        userContext.clear(sessionToken);
    }
}

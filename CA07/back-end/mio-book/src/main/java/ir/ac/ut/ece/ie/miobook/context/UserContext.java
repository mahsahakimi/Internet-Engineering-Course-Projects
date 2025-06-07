package ir.ac.ut.ece.ie.miobook.context;

import ir.ac.ut.ece.ie.miobook.model.User;
import ir.ac.ut.ece.ie.miobook.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.data.redis.core.RedisTemplate;


@Component
public class UserContext {

    private final RedisTemplate<String, String> redisTemplate;
    private final UserRepository userRepository;

    @Autowired
    public UserContext(RedisTemplate<String, String> redisTemplate,
                       UserRepository userRepository) {
        this.redisTemplate = redisTemplate;
        this.userRepository = userRepository;
    }

    public User getLoggedInUser(String sessionId) {
        if (sessionId == null) return null;

        String username = redisTemplate.opsForValue().get(sessionId);
        if (username == null) return null;

        return userRepository.findByUsername(username).orElse(null);
    }

    public void clear(String sessionId) {
        if (sessionId != null) {
            redisTemplate.delete(sessionId);
        }
    }
}
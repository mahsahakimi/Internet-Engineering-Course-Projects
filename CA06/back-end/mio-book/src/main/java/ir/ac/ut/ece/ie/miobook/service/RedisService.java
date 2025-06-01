package ir.ac.ut.ece.ie.miobook.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    private static final long SESSION_TIMEOUT = 20;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public String createSession(Integer userId) {
        String sessionToken = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(
                sessionToken,
                userId.toString(),
                SESSION_TIMEOUT,
                TimeUnit.MINUTES
        );
        return sessionToken;
    }

    public Integer validateSession(String sessionToken) {
        String userId = redisTemplate.opsForValue().get(sessionToken);
        if (userId != null) {
            redisTemplate.expire(sessionToken, SESSION_TIMEOUT, TimeUnit.MINUTES);
            return Integer.parseInt(userId);
        }
        return null;
    }

    public void invalidateSession(String sessionToken) {
        redisTemplate.delete(sessionToken);
    }
}
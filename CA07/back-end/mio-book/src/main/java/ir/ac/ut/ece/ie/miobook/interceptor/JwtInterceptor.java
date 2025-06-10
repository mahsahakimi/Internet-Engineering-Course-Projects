package ir.ac.ut.ece.ie.miobook.interceptor;

import ir.ac.ut.ece.ie.miobook.exceptions.UserNotLoggedInException;
import ir.ac.ut.ece.ie.miobook.model.User;
import ir.ac.ut.ece.ie.miobook.repository.UserRepository;
import ir.ac.ut.ece.ie.miobook.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import org.springframework.lang.NonNull;
import java.time.Instant;
import java.util.Optional;

@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, 
                            @NonNull HttpServletResponse response, 
                            @NonNull Object handler) throws Exception {

        String token = request.getHeader("Authorization");

        if (token == null || token.isBlank()) {
            throw new UserNotLoggedInException();
        }

        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        try {
            Claims claims = JwtUtil.parseToken(token);

            String username = claims.get("username", String.class);
            String email = claims.get("email", String.class);
            Instant issuedAt = claims.getIssuedAt().toInstant();
            Instant expiration = claims.getExpiration().toInstant();

            if (username == null || email == null || issuedAt == null || expiration == null) {
                throw new UserNotLoggedInException();
            }

            if(issuedAt.isAfter(Instant.now())){
                throw new UserNotLoggedInException();
            }

            if (Instant.now().isAfter(expiration)) {
                throw new UserNotLoggedInException();
            }

            Optional<User> userOptional = userRepository.findByUsername(username);
            if (userOptional.isEmpty()) {
                throw new UserNotLoggedInException();
            }

            request.setAttribute("loggedInUser", userOptional.get());

        } catch (JwtException e) {
            throw new UserNotLoggedInException();
        }

        return true;
    }
}
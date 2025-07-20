package ir.ac.ut.ece.ie.miobook.service;

import ir.ac.ut.ece.ie.miobook.exceptions.IncorrectPasswordException;
import ir.ac.ut.ece.ie.miobook.exceptions.UserNotFoundException;
import ir.ac.ut.ece.ie.miobook.model.Customer;
import ir.ac.ut.ece.ie.miobook.model.User;
import ir.ac.ut.ece.ie.miobook.model.Wallet;
import ir.ac.ut.ece.ie.miobook.repository.UserRepository;
import ir.ac.ut.ece.ie.miobook.utils.JwtUtil;
import ir.ac.ut.ece.ie.miobook.utils.PasswordHasher;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthService {

    private final UserRepository userRepository;

    // Inject values from application.properties
    @Value("${google.client-id}")
    private String googleClientId;

    @Value("${google.client-secret}")
    private String googleClientSecret;

    public AuthService(UserRepository userRepository ) {
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


    public Map<String, Object> processGoogleLogin(String code) {
        String accessToken = getAccessTokenFromGoogle(code);
        Map<String, Object> userInfo = getUserInfoFromGoogle(accessToken);

        String email = (String) userInfo.get("email");
        String name  = (String) userInfo.get("given_name");

        User tempuser1 = userRepository.findByEmail(email).orElse(null);
        User tempuser2 = userRepository.findByUsername(name).orElse(null);
        User theUser = tempuser2;

        if (tempuser1 == null && tempuser2 == null) {
            Customer customer = new Customer(name, null, null, email, null);
            Wallet w = new Wallet(customer);
            customer.setWallet(w);
            theUser = customer;
            userRepository.save(theUser);
            System.out.println("New customer : " + name);

        } else if (tempuser1 != null && tempuser2 != null && !tempuser1.getUsername().equals(tempuser1.getUsername())) {
            throw new RuntimeException("Failed to Fetch User by this account!");

        } else {
            if (tempuser1 != null) {
                theUser = tempuser1;
            }
        }


        Map<String, Object> claims = new HashMap<>();
        claims.put("username", theUser.getUsername());
        claims.put("email", theUser.getEmail());

        String token = JwtUtil.generateToken(theUser.getUsername(), claims);

        Map<String, Object> userInf = new HashMap<>();
        userInf.put("username", theUser.getUsername());
        userInf.put("email", theUser.getEmail());
        userInf.put("role", theUser.getRole());
        userInf.put("token", token);
        return userInf;
    }

    private String getAccessTokenFromGoogle(String code) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        String redirectUri = "http://localhost:5555/google-callback";

        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("code", code);
        body.add("client_id", googleClientId);
        body.add("client_secret", googleClientSecret);
        body.add("redirect_uri", redirectUri);
        body.add("grant_type", "authorization_code");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
            "https://oauth2.googleapis.com/token",
            HttpMethod.POST,
            request,
            new ParameterizedTypeReference<Map<String, Object>>() {}
        );

        if (response.getStatusCode() != HttpStatus.OK)
            throw new RuntimeException("Failed to get access token from Google");

        Map<String, Object> responseBody = response.getBody(); // now safely dereferenced
        if(responseBody == null){
            return null;
        }
        return (String) responseBody.get("access_token");
    }

    private Map<String, Object> getUserInfoFromGoogle(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                "https://www.googleapis.com/oauth2/v2/userinfo",
                HttpMethod.GET,
                entity,
                Map.class
        );

        if (response.getStatusCode() != HttpStatus.OK)
            throw new RuntimeException("Failed to get user info from Google");

        return response.getBody();
    }


}
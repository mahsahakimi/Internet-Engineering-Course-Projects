package ir.ac.ut.ece.ie.miobook.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import ir.ac.ut.ece.ie.miobook.database.UserDatabase;
import ir.ac.ut.ece.ie.miobook.model.Role;
import ir.ac.ut.ece.ie.miobook.model.User;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ShowUserDetailsService {

    public static String getUserDetails(String username) throws IOException {
        User user = UserDatabase.getInstance().findUserByUsername(username);

        if (user == null) {
            throw new IllegalArgumentException("User not found.");
        }

        Map<String, Object> userData = new HashMap<>();
        userData.put("username", user.getUsername());
        userData.put("role", user.getRole());
        userData.put("email", user.getEmail());
        userData.put("address", user.getAddress());

        if (user.getRole() == Role.customer) {
            userData.put("balance", user.getBalance());
        }

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "User details retrieved successfully.");
        response.put("data", userData);

        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(response);
    }
}
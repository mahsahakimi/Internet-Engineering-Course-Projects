package ir.ac.ut.ece.ie.miobook.service;

import ir.ac.ut.ece.ie.miobook.database.UserDatabase;
import ir.ac.ut.ece.ie.miobook.model.Address;
import ir.ac.ut.ece.ie.miobook.model.Role;
import ir.ac.ut.ece.ie.miobook.model.User;
import ir.ac.ut.ece.ie.miobook.validator.AddressValidator;
import ir.ac.ut.ece.ie.miobook.validator.EmailValidator;
import ir.ac.ut.ece.ie.miobook.validator.PasswordValidator;
import ir.ac.ut.ece.ie.miobook.validator.UsernameValidator;

public class AddUserService {

    public static void addUser(String role, String username, String password, String email, Address address) {
        try {
            Role.valueOf(role);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid role: " + role + ". Valid roles are: " + java.util.Arrays.toString(Role.values()));
        }

        if (!UsernameValidator.isValid(username)) {
            throw new IllegalArgumentException("Username is not valid: " + username);
        }

        if (!PasswordValidator.isValid(password)) {
            throw new IllegalArgumentException("Password is not valid: " + password);
        }

        if (!EmailValidator.isValid(email)) {
            throw new IllegalArgumentException("Email is not valid: " + email);
        }

        if (!AddressValidator.isValid(address.getCountry(), address.getCity())) {
            throw new IllegalArgumentException("Address is not valid: " + address.getCountry() + ", " + address.getCity());
        }

        User user = new User(role, username, password, email, address);
        UserDatabase.getInstance().addUser(user);
    }
}
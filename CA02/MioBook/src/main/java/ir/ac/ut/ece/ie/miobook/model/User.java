package ir.ac.ut.ece.ie.miobook.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import ir.ac.ut.ece.ie.miobook.validator.AddressValidator;
import ir.ac.ut.ece.ie.miobook.validator.EmailValidator;
import ir.ac.ut.ece.ie.miobook.validator.PasswordValidator;
import ir.ac.ut.ece.ie.miobook.validator.UsernameValidator;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class User {
    Role role;
    String username;
    String password;
    String email;
    Address address;
    int balance = 0;
    List<Book> cart = new ArrayList<>();
    List<Purchase> purchases = new ArrayList<>();
    List<Book> ownedBooks = new ArrayList<>();

    public User(@JsonProperty("role") String role, @JsonProperty("username") String username,
                @JsonProperty("password") String password, @JsonProperty("email") String email,
                @JsonProperty("address") Address address) {
        try {
            this.role = Role.valueOf(role);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid role: " + role + ". Valid roles are: " + java.util.Arrays.toString(Role.values()));
        }

        if (UsernameValidator.isValid(username)) {
            this.username = username;
        } else {
            throw new IllegalArgumentException("Username is not valid: " + username);
        }

        if (PasswordValidator.isValid(password)) {
            this.password = password;
        } else {
            throw new IllegalArgumentException("Password is not valid: " + password);
        }

        if (EmailValidator.isValid(email)) {
            this.email = email;
        } else {
            throw new IllegalArgumentException("Email is not valid: " + email);
        }

        if (AddressValidator.isValid(address.getCountry(), address.getCity())) {
            this.address = address;
        } else {
            throw new IllegalArgumentException("Address is not valid: " + address.getCountry() + ", " + address.getCity());
        }
    }

    public void addToCart(Book book){
        this.cart.add(book);
    }

    public void clearCart() {
        this.cart.clear();
    }

    public void removeBook(Book book){
        this.cart.remove(book);
    }

    public void addToPurchase(Purchase p, List<Book> cart){
        this.purchases.add(p);
        // add books to user owned books
        ownedBooks.addAll(cart);
    }

    @Override
    public String toString() {
        return "User{" +
                "role=" + role +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", address=" + address +
                ", balance=" + balance +
                ", cart=" + cart +
                '}';
    }
}
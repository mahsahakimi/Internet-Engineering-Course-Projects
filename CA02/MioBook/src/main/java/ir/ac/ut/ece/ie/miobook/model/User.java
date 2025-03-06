package ir.ac.ut.ece.ie.miobook.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import ir.ac.ut.ece.ie.miobook.validator.AddressValidator;
import ir.ac.ut.ece.ie.miobook.validator.EmailValidator;
import ir.ac.ut.ece.ie.miobook.validator.PasswordValidator;
import ir.ac.ut.ece.ie.miobook.validator.UsernameValidator;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class User {
    Role role;
    String username;
    String password;
    String email;
    Address address;
    int balance = 0;
    List<CartElement> cart = new ArrayList<>();
    List<Purchase> purchases = new ArrayList<>();
    List<Book> ownedBooks = new ArrayList<>();
    Map<Book, LocalDate> borrowedBooks = new HashMap<>();


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

    public void addToCart(CartElement book){
        this.cart.add(book);
    }

    public void clearCart() {
        this.cart.clear();
    }

    public void removeBook(Book book){
        this.cart.removeIf(ce -> ce.book.title.equals(book.getTitle()));
    }

    public void addToPurchase(Purchase p, List<CartElement> cart){
        LocalDate tempDate;
        this.purchases.add(p);
        // add books to user owned books
        for (CartElement ce : cart){
            if(ce.getDays() == 0){
                ownedBooks.add(ce.book);
            }
            else{
                tempDate = LocalDate.now().plusDays(ce.getDays());
                borrowedBooks.put(ce.getBook(), tempDate);
            }
        }
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
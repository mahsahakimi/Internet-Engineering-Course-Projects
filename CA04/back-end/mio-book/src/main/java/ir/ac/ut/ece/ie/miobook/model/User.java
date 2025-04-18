package ir.ac.ut.ece.ie.miobook.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor
public class User {
    private Role role;
    private String username;
    private String password;
    private String email;
    private Address address;
    private int balance;
    List<CartElement> cart = new ArrayList<>();
    List<Purchase> purchases = new ArrayList<>();
    List<Book> ownedBooks = new ArrayList<>();
    List<BorrowedBook> borrowedBooks = new ArrayList<>();

    public User(String role, String username, String password, String email, Address address) {
        this.role = Role.valueOf(role);
        this.username = username;
        this.password = password;
        this.email = email;
        this.address = address;
        this.balance = 0;
    }

    public void removeBookFromCart(Book book) {
        Optional<CartElement> elementToRemove = cart.stream()
                .filter(cartElement -> cartElement.getBook().equals(book))
                .findFirst();

        elementToRemove.ifPresent(cart::remove);
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
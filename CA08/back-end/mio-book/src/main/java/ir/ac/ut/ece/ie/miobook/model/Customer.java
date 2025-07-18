package ir.ac.ut.ece.ie.miobook.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@DiscriminatorValue("CUSTOMER")
@PrimaryKeyJoinColumn(name = "username")
public class Customer extends User {

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartElement> cart = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_username")
    List<Purchase> purchases = new ArrayList<>();

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserBook> library = new ArrayList<>();

    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private Wallet wallet;

    public Customer(String username, String password, String salt, String email, Address address) {
        super(Role.CUSTOMER.name(), username, password, salt, email, address);
    }
    
    public void removeBookFromCart(Book book) {
        cart.removeIf(ce -> ce.getBook().getTitle().equals(book.getTitle()));
    }
}
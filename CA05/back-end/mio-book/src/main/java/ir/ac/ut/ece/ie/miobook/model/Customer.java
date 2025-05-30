package ir.ac.ut.ece.ie.miobook.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Getter
@Setter
@NoArgsConstructor
@DiscriminatorValue("CUSTOMER")
@PrimaryKeyJoinColumn(name = "username")
public class Customer extends User {

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_username")
    List<CartElement> cart = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_username")
    List<Purchase> purchases = new ArrayList<>();

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserBook> library = new ArrayList<>();

    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private Wallet wallet;

    public Customer(String username, String password, String email, Address address) {
        super(Role.CUSTOMER.name(), username, password, email, address);
    }

    public void removeBookFromCart(Book book) {
        Optional<CartElement> elementToRemove = cart.stream()
                .filter(cartElement -> cartElement.getBook().equals(book))
                .findFirst();

        elementToRemove.ifPresent(cart::remove);
    }
}
package ir.ac.ut.ece.ie.miobook.model;

import ir.ac.ut.ece.ie.miobook.repository.WalletRepository;
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

    @ManyToMany
    @JoinTable(
            name = "user_owned_books",
            joinColumns = @JoinColumn(name = "user_username"),
            inverseJoinColumns = @JoinColumn(name = "book_id")
    )
    List<Book> ownedBooks = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_username")
    List<BorrowedBook> borrowedBooks = new ArrayList<>();

    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private Wallet wallet;

    public Customer(String username, String password, String email, Address address) {
        super(Role.CUSTOMER.name(), username, password, email, address);
        this.wallet = new Wallet(this);
    }

    public void removeBookFromCart(Book book) {
        Optional<CartElement> elementToRemove = cart.stream()
                .filter(cartElement -> cartElement.getBook().equals(book))
                .findFirst();

        elementToRemove.ifPresent(cart::remove);
    }
}
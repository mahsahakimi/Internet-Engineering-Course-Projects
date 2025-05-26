package ir.ac.ut.ece.ie.miobook.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "Wallet")
@Table(name = "wallet")
@Getter
@Setter
@NoArgsConstructor
public class Wallet {

    @Id
    @Column(name = "customer_username")
    private String username;

    @OneToOne
    @MapsId  // Shares primary key with Customer
    @JoinColumn(name = "customer_username",
            foreignKey = @ForeignKey(name = "fk_wallet_customer"))
    private Customer customer;

    @Column(nullable = false)
    private int balance = 0;

    public Wallet(Customer customer) {
        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null");
        }
        this.customer = customer;
        this.username = customer.getUsername();
    }

    public synchronized void deposit(int amount) {
        validateAmount(amount);
        this.balance += amount;
    }

    private void validateAmount(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException(
                    String.format("Amount must be positive. Received: %d", amount)
            );
        }
    }

    // Setter only for balance with package-private visibility
    // Used only by JPA/reflection, not for business operations
    // @Setter(AccessLevel.PACKAGE)
    private void setBalance(int balance) {
        this.balance = balance;
    }
}
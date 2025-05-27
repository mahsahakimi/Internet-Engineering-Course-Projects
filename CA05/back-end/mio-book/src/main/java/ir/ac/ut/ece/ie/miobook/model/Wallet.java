package ir.ac.ut.ece.ie.miobook.model;

import ir.ac.ut.ece.ie.miobook.exceptions.*;
import jakarta.persistence.*;
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
        if (amount <= 0) {
            throw new InvalidCreditAmountException();
        }
        if (amount < 100) {
            throw new MinimumCreditException();
        }
        this.balance += amount;
    }
    public synchronized void withdraw(int amount) {
        if (balance - amount < 0) {
            throw new InsufficientBalanceException();
        }
        this.balance -= amount;
    }

    // Setter only for balance with package-private visibility
    // Used only by JPA/reflection, not for business operations
    private void setBalance(int balance) {
        this.balance = balance;
    }
}
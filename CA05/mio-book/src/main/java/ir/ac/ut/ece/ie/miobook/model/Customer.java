package ir.ac.ut.ece.ie.miobook.model;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@PrimaryKeyJoinColumn(name = "username")
public class Customer extends User {

    public Customer(String username, String password, String email, Address address) {
        super(Role.CUSTOMER.name(), username, password, email, address);
    }
}
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
public class Admin extends User {

    public Admin(String username, String password, String email, Address address) {
        super(Role.ADMIN.name(), username, password, email, address);
    }
}
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
@DiscriminatorValue("ADMIN")
@PrimaryKeyJoinColumn(name = "username")
public class Admin extends User {

    public Admin(String username, String password, String email, Address address) {
        super(Role.ADMIN.name(), username, password, email, address);
    }
}
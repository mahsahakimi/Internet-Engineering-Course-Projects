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

    @OneToMany(mappedBy = "admin", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Author> managedAuthors = new ArrayList<>();

    public Admin(String username, String password, String email, Address address) {
        super(Role.ADMIN.name(), username, password, email, address);
    }

    public void addManagedAuthor(Author author) {
        author.setAdmin(this);
        this.managedAuthors.add(author);
    }

    public void removeManagedAuthor(Author author) {
        this.managedAuthors.remove(author);
        author.setAdmin(null);
    }
}
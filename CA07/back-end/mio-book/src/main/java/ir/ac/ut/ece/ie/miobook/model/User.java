package ir.ac.ut.ece.ie.miobook.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "User")
@Table(name = "user")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "role", discriminatorType = DiscriminatorType.STRING)
@Getter
@Setter
@NoArgsConstructor
public class User {
    @Enumerated(EnumType.STRING)
    @Column(name = "role", insertable = false, updatable = false)
    private Role role;

    @Column(nullable = false, length = 255)
    @Id
    private String username;

    @Column(nullable = true)
    private String password;

    @Column(nullable = true)
    private String salt;

    @Column(columnDefinition = "TEXT", nullable = false, unique = true)
    private String email;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Address address;

    public User(String role, String username, String password, String salt, String email, Address address) {
        this.role = Role.valueOf(role);
        this.username = username;
        this.password = password;
        this.salt = salt;
        this.email = email;
        this.address = address;
    }

    @Override
    public String toString() {
        return "User{" +
                "role=" + role +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", address=" + address +
                '}';
    }
}
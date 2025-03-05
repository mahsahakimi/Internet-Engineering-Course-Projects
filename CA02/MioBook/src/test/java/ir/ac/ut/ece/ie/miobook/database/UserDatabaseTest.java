package ir.ac.ut.ece.ie.miobook.database;

import ir.ac.ut.ece.ie.miobook.model.Address;
import ir.ac.ut.ece.ie.miobook.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserDatabaseTest {

    private UserDatabase database;
    private User user1;
    private User user2;

    @BeforeEach
    public void setUp() {
        database = UserDatabase.getInstance();
        Address address1 = new Address("USA", "New York");
        Address address2 = new Address("Canada", "Toronto");
        user1 = new User("customer", "Cool_User", "password123", "email@email.com", address1);
        user2 = new User("admin", "Another_User", "securePass", "another@email.com", address2);
    }

    @AfterEach
    public void tearDown() {
        database = UserDatabase.getInstance();
        database.getUsers().clear();
        user1 = null;
        user2 = null;
    }

    @Test
    public void testAddUserSuccess() {
        database.addUser(user1);
        database.addUser(user2);
    }

    @Test
    public void testAddUserDuplicateUsername() {
        database.addUser(user1);
        Address address = new Address("UK", "London");
        User duplicateUsernameUser = new User("customer", "Cool_User", "newPass", "different@email.com", address);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            database.addUser(duplicateUsernameUser);
        });

        assertEquals("Username already exists: Cool_User", exception.getMessage());
    }

    @Test
    public void testAddUserDuplicateEmail() {
        database.addUser(user1);
        Address address = new Address("France", "Paris");
        User duplicateEmailUser = new User("admin", "Different_User", "uniquePass", "email@email.com", address);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            database.addUser(duplicateEmailUser);
        });

        assertEquals("Email already exists: email@email.com", exception.getMessage());
    }
}
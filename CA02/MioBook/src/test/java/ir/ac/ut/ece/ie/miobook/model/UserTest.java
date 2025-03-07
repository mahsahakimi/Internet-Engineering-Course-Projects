package ir.ac.ut.ece.ie.miobook.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    @Test
    public void testValidUserCreation() {
        Address address = new Address("USA", "New York");
        User user = new User("customer", "Cool_User", "password123", "email@example.com", address);

        assertEquals(Role.customer, user.getRole());
        assertEquals("Cool_User", user.getUsername());
        assertEquals("email@example.com", user.getEmail());
        assertEquals(address, user.getAddress());
        assertEquals(0, user.getBalance());
    }
}

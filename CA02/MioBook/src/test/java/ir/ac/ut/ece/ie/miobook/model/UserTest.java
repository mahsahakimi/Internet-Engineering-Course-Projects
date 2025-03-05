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

    @Test
    public void testInvalidRole() {
        Address address = new Address("USA", "New York");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new User("invalidRole", "Cool_User", "password123", "email@example.com", address);
        });

        assertTrue(exception.getMessage().startsWith("Invalid role: invalidRole"));
    }

    @Test
    public void testInvalidUsername() {
        Address address = new Address("USA", "New York");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new User("customer", "Cool User", "password123", "email@example.com", address);
        });

        assertEquals("Username is not valid: Cool User", exception.getMessage());
    }

    @Test
    public void testInvalidPassword() {
        Address address = new Address("USA", "New York");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new User("customer", "Cool_User", "123", "email@example.com", address);
        });

        assertEquals("Password is not valid: 123", exception.getMessage());
    }

    @Test
    public void testInvalidEmail() {
        Address address = new Address("USA", "New York");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new User("customer", "Cool_User", "password123", "invalidEmail", address);
        });

        assertEquals("Email is not valid: invalidEmail", exception.getMessage());
    }

    @Test
    public void testInvalidAddressEmptyCountry() {
        Address invalidAddress = new Address("", "New York");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new User("customer", "Cool_User", "password123", "email@example.com", invalidAddress);
        });

        assertEquals("Address is not valid: , New York", exception.getMessage());
    }

    @Test
    public void testInvalidAddressEmptyCity() {
        Address invalidAddress = new Address("USA", "");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new User("customer", "Cool_User", "password123", "email@example.com", invalidAddress);
        });

        assertEquals("Address is not valid: USA, ", exception.getMessage());
    }

    @Test
    public void testInvalidAddressNullCountry() {
        Address invalidAddress = new Address(null, "New York");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new User("customer", "Cool_User", "password123", "email@example.com", invalidAddress);
        });

        assertEquals("Address is not valid: null, New York", exception.getMessage());
    }

    @Test
    public void testInvalidAddressNullCity() {
        Address invalidAddress = new Address("USA", null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new User("customer", "Cool_User", "password123", "email@example.com", invalidAddress);
        });

        assertEquals("Address is not valid: USA, null", exception.getMessage());
    }
}

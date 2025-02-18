package edu.ut.ie.hotelreservation;

import edu.ut.ie.hotelreservation.model.Customer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class CustomerTest {

    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer(12345, "John Doe", "09101231212", 18);
    }

    @AfterEach
    void tearDown() {
        customer = null;
    }

    @Test
    void testCustomerCreation() {
        assertEquals(12345, customer.getSsn());
        assertEquals("John Doe", customer.getName());
        assertEquals("09101231212", customer.getPhone());
        assertEquals(18, customer.getAge());
    }

    @Test
    void testCustomerCreationWithInvalidAge() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Customer(12345, "John Doe", "09101231212", -1);
        });
    }
}

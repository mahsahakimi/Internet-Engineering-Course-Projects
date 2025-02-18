package edu.ut.ie.hotelreservation;

import edu.ut.ie.hotelreservation.model.Booking;
import edu.ut.ie.hotelreservation.model.Customer;
import edu.ut.ie.hotelreservation.model.Room;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.format.DateTimeParseException;

import static org.junit.jupiter.api.Assertions.*;

public class BookingTest {

    private Booking booking;
    private Customer customer;
    private Room room;

    @BeforeEach
    void setUp() {
        customer = new Customer(12345, "John Doe", "09101231212", 18);
        room = new Room(101, 3);
        booking = new Booking(1, customer, room, "2023-03-15 08:00:00", "2023-03-20 16:00:00");
    }

    @AfterEach
    void tearDown() {
        booking = null;
        customer = null;
        room = null;
    }

    @Test
    void testBookingCreation() {
        assertEquals(1, booking.getId());
        assertEquals(customer, booking.getCustomer());
        assertEquals(room, booking.getRoom());
        assertEquals("2023-03-15 08:00:00", booking.getCheckInDate());
        assertEquals("2023-03-20 16:00:00", booking.getCheckOutDate());
    }

    @Test
    void testGetStayDurationInDays() {
        assertEquals(5, booking.getStayDurationInDays());
    }

    @Test
    void testInvalidCheckInDate() {
        assertThrows(DateTimeParseException.class, () -> {
            new Booking(1, customer, room, "invalid-date", "2023-03-20 16:00:00");
        });
    }

    @Test
    void testInvalidCheckOutDate() {
        assertThrows(DateTimeParseException.class, () -> {
            new Booking(1, customer, room, "2023-03-15 08:00:00", "invalid-date");
        });
    }

    @Test
    void testCheckOutBeforeCheckIn() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Booking(1, customer, room, "2023-03-20 16:00:00", "2023-03-15 08:00:00");
        });
    }
}

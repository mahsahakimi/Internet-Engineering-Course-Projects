package edu.ut.ie.hotelreservation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class DataValidationTest {

    @Test
    void testDataValidation() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode data = objectMapper.readTree(new File("src/main/resources/data.json"));

        assertNotNull(data, "Data is missing. Ensure data.json exists and is valid.");

        validateCustomers(data);
        validateRooms(data);
        validateBookings(data);
    }

    void validateCustomers(JsonNode data) {
        Set<Integer> customerIds = new HashSet<>();
        JsonNode customers = data.get("customers");
        assertNotNull(customers, "Customers array is missing");
        assertTrue(customers.isArray(), "Customers should be an array.");

        for (JsonNode customer : customers) {
            assertNotNull(customer.get("ssn"), "Customer SSN is missing");
            int ssn = customer.get("ssn").asInt();
            assertTrue(customerIds.add(ssn), "Duplicate customer SSN: " + ssn);
            assertNotNull(customer.get("name"), "Customer name is missing");
            assertNotNull(customer.get("phone"), "Customer phone is missing");
            assertTrue(customer.get("age").isInt(), "Customer age should be an integer.");
            assertTrue(customer.get("age").asInt() >= 0, "Customer age is less than 0");
        }
    }

    void validateRooms(JsonNode data) {
        Set<Integer> roomIds = new HashSet<>();
        JsonNode rooms = data.get("rooms");
        assertNotNull(rooms, "Rooms array is missing");
        assertTrue(rooms.isArray(), "Rooms should be an array.");

        for (JsonNode room : rooms) {
            assertNotNull(room.get("id"), "Room ID is missing");
            int id = room.get("id").asInt();
            assertTrue(roomIds.add(id), "Duplicate room ID: " + id);
            assertNotNull(room.get("capacity"), "Room capacity is missing");
            assertTrue(room.get("capacity").isInt(), "Room capacity should be an integer.");
            assertTrue(room.get("capacity").asInt() > 0, "Room capacity is not positive");
        }
    }

    void validateBookings(JsonNode data) {
        Set<Integer> bookingIds = new HashSet<>();
        JsonNode bookings = data.get("bookings");
        assertNotNull(bookings, "Bookings array is missing");
        assertTrue(bookings.isArray(), "Bookings should be an array.");

        JsonNode rooms = data.get("rooms");
        JsonNode customers = data.get("customers");

        for (JsonNode booking : bookings) {
            assertNotNull(booking.get("id"), "Booking ID is missing");
            int id = booking.get("id").asInt();
            assertTrue(bookingIds.add(id), "Duplicate booking ID: " + id);

            assertNotNull(booking.get("room_id"), "Booking room_id is missing");
            assertNotNull(booking.get("customer_id"), "Booking customer_id is missing");

            int roomId = booking.get("room_id").asInt();
            int customerId = booking.get("customer_id").asInt();

            assertTrue(roomExists(rooms, roomId), "Booking room_id does not exist: " + roomId);
            assertTrue(customerExists(customers, customerId), "Booking customer_id does not exist: " + customerId);

            assertNotNull(booking.get("check_in"), "Booking check_in is missing");
            assertNotNull(booking.get("check_out"), "Booking check_out is missing");
        }
    }

    boolean roomExists(JsonNode rooms, int roomId) {
        for (JsonNode room : rooms) {
            if (room.get("id").asInt() == roomId) {
                return true;
            }
        }
        return false;
    }

    boolean customerExists(JsonNode customers, int customerId) {
        for (JsonNode customer : customers) {
            if (customer.get("ssn").asInt() == customerId) {
                return true;
            }
        }
        return false;
    }
}

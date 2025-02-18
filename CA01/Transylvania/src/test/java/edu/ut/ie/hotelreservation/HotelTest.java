package edu.ut.ie.hotelreservation;

import com.jayway.jsonpath.JsonPath;
import edu.ut.ie.hotelreservation.dto.BookingsByRoomDTO;
import edu.ut.ie.hotelreservation.dto.RoomWithBookingsDTO;
import edu.ut.ie.hotelreservation.model.Booking;
import edu.ut.ie.hotelreservation.model.Customer;
import edu.ut.ie.hotelreservation.model.Hotel;
import edu.ut.ie.hotelreservation.model.Room;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.List;

public class HotelTest {

    private Hotel hotel;
    private Customer customer1;
    private Customer customer2;
    private Room room1;
    private Room room2;
    private Room room3;
    private Booking booking1;
    private Booking booking2;

    @BeforeEach
    void setUp() {
        hotel = new Hotel();
        customer1 = new Customer(1, "John Doe", "09177020690", 30);
        customer2 = new Customer(2, "Jane Smith", "09106764528", 25);
        room1 = new Room(101, 2);
        room2 = new Room(102, 3);
        room3 = new Room(103, 1);

        hotel.getCustomers().add(customer1);
        hotel.getCustomers().add(customer2);
        hotel.getRooms().add(room1);
        hotel.getRooms().add(room2);
        hotel.getRooms().add(room3);

        booking1 = new Booking(1, customer1, room1, "2024-01-01 08:00:00", "2024-01-03 16:00:00");
        booking2 = new Booking(2, customer2, room2, "2024-01-05 09:00:00", "2024-01-07 16:00:00");
        hotel.getBookings().add(booking1);
        hotel.getBookings().add(booking2);
    }

    @AfterEach
    void tearDown() {
        hotel = null;
        customer1 = null;
        customer2 = null;
        room1 = null;
        room2 = null;
        room3 = null;
        booking1 = null;
        booking2 = null;
    }

    @Test
    void testGetRoomsWithMinCapacity() {
        List<Room> availableRooms = hotel.getRooms(2);
        assertEquals(2, availableRooms.size());
        assertTrue(availableRooms.contains(room1));
        assertTrue(availableRooms.contains(room2));

        List<Room> noRooms = hotel.getRooms(4);
        assertEquals(0, noRooms.size());
    }

    @Test
    void testGetOldestCustomerName() {
        assertEquals("John Doe", hotel.getOldestCustomerName());
    }

    @Test
    void testGetCustomerPhonesByRoomNumber() {
        List<String> phones = hotel.getCustomerPhonesByRoomNumber(101);
        assertEquals(1, phones.size());
        assertTrue(phones.contains("09177020690"));
    }

    @Test
    void testLogState() throws IOException {
        String log = hotel.logState();

        assertEquals(3, (int) JsonPath.read(log, "$.length()"));

        assertEquals(101, (int) JsonPath.read(log, "$[0].room_id"));
        assertEquals(2, (int) JsonPath.read(log, "$[0].capacity"));
        assertEquals(1, (int) JsonPath.read(log, "$[0].bookings.length()"));
        assertEquals(1, (int) JsonPath.read(log, "$[0].bookings[0].id"));
        assertEquals("John Doe", JsonPath.read(log, "$[0].bookings[0].customer.name"));
        assertEquals("09177020690", JsonPath.read(log, "$[0].bookings[0].customer.phone"));
        assertEquals(30, (int) JsonPath.read(log, "$[0].bookings[0].customer.age"));
        assertEquals("2024-01-01 08:00:00", JsonPath.read(log, "$[0].bookings[0].check_in"));
        assertEquals("2024-01-03 16:00:00", JsonPath.read(log, "$[0].bookings[0].check_out"));

        assertEquals(102, (int) JsonPath.read(log, "$[1].room_id"));
        assertEquals(3, (int) JsonPath.read(log, "$[1].capacity"));
        assertEquals(1, (int) JsonPath.read(log, "$[1].bookings.length()"));
        assertEquals(2, (int) JsonPath.read(log, "$[1].bookings[0].id"));
        assertEquals("Jane Smith", JsonPath.read(log, "$[1].bookings[0].customer.name"));
        assertEquals("09106764528", JsonPath.read(log, "$[1].bookings[0].customer.phone"));
        assertEquals(25, (int) JsonPath.read(log, "$[1].bookings[0].customer.age"));
        assertEquals("2024-01-05 09:00:00", JsonPath.read(log, "$[1].bookings[0].check_in"));
        assertEquals("2024-01-07 16:00:00", JsonPath.read(log, "$[1].bookings[0].check_out"));

        assertEquals(103, (int) JsonPath.read(log, "$[2].room_id"));
        assertEquals(1, (int) JsonPath.read(log, "$[2].capacity"));
        assertEquals(0, (int) JsonPath.read(log, "$[2].bookings.length()"));
    }

    @Test
    void testGetRoomsWithBookings() {
        List<RoomWithBookingsDTO> roomsWithBookings = hotel.getRoomsWithBookings();

        assertNotNull(roomsWithBookings);
        assertEquals(3, roomsWithBookings.size());

        RoomWithBookingsDTO room101 = findRoomWithBookings(roomsWithBookings, 101);
        assertNotNull(room101);
        assertEquals(2, room101.getCapacity());
        assertEquals(1, room101.getBookings().size());
        assertEquals(1, room101.getBookings().get(0).getId());
        assertEquals("John Doe", room101.getBookings().get(0).getCustomer().getName());

        RoomWithBookingsDTO room102 = findRoomWithBookings(roomsWithBookings, 102);
        assertNotNull(room102);
        assertEquals(3, room102.getCapacity());
        assertEquals(1, room102.getBookings().size());
        assertEquals(2, room102.getBookings().get(0).getId());
        assertEquals("Jane Smith", room102.getBookings().get(0).getCustomer().getName());

        RoomWithBookingsDTO room103 = findRoomWithBookings(roomsWithBookings, 103);
        assertNotNull(room103);
        assertEquals(1, room103.getCapacity());
        assertEquals(0, room103.getBookings().size());
    }

    // Helper function to find a RoomWithBookingsDTO by room ID
    private RoomWithBookingsDTO findRoomWithBookings(List<RoomWithBookingsDTO> rooms, int roomId) {
        for (RoomWithBookingsDTO room : rooms) {
            if (room.getRoom_id() == roomId) {
                return room;
            }
        }
        return null;
    }

    @Test
    void testGetBookingsByRoom() {
        List<BookingsByRoomDTO> bookingsByRoom101 = hotel.getBookingsByRoom(101);

        assertEquals(1, bookingsByRoom101.size());
        assertEquals(1, bookingsByRoom101.get(0).getId());
        assertEquals("John Doe", bookingsByRoom101.get(0).getCustomer().getName());
        assertEquals("09177020690", bookingsByRoom101.get(0).getCustomer().getPhone());
        assertEquals(30, bookingsByRoom101.get(0).getCustomer().getAge());
        assertEquals("2024-01-01 08:00:00", bookingsByRoom101.get(0).getCheck_in());
        assertEquals("2024-01-03 16:00:00", bookingsByRoom101.get(0).getCheck_out());

        List<BookingsByRoomDTO> bookingsByRoom102 = hotel.getBookingsByRoom(102);

        assertEquals(1, bookingsByRoom102.size());
        assertEquals(2, bookingsByRoom102.get(0).getId());
        assertEquals("Jane Smith", bookingsByRoom102.get(0).getCustomer().getName());
        assertEquals("09106764528", bookingsByRoom102.get(0).getCustomer().getPhone());
        assertEquals(25, bookingsByRoom102.get(0).getCustomer().getAge());
        assertEquals("2024-01-05 09:00:00", bookingsByRoom102.get(0).getCheck_in());
        assertEquals("2024-01-07 16:00:00", bookingsByRoom102.get(0).getCheck_out());

        List<BookingsByRoomDTO> bookingsByRoom103 = hotel.getBookingsByRoom(103);

        assertEquals(0, bookingsByRoom103.size());
    }
}

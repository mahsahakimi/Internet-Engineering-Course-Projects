package edu.ut.ie.hotelreservation;

import edu.ut.ie.hotelreservation.dto.BookingDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class BookingDTOTest {

    private BookingDTO bookingDTO;

    @BeforeEach
    void setUp() {
        bookingDTO = new BookingDTO(1, "2024-01-01 08:00:00", "2024-01-03 16:00:00", 101, 1);
    }

    @AfterEach
    void tearDown() {
        bookingDTO = null;
    }

    @Test
    void testBookingDTOCreation() {
        assertEquals(1, bookingDTO.getId());
        assertEquals("2024-01-01 08:00:00", bookingDTO.getCheckIn());
        assertEquals("2024-01-03 16:00:00", bookingDTO.getCheckOut());
        assertEquals(101, bookingDTO.getRoomId());
        assertEquals(1, bookingDTO.getCustomerId());
    }

    @Test
    void testBookingDTOGettersAndSetters() {
        bookingDTO.setId(2);
        bookingDTO.setCheckIn("2024-01-05 08:00:00");
        bookingDTO.setCheckOut("2024-01-07 16:00:00");
        bookingDTO.setRoomId(102);
        bookingDTO.setCustomerId(2);

        assertEquals(2, bookingDTO.getId());
        assertEquals("2024-01-05 08:00:00", bookingDTO.getCheckIn());
        assertEquals("2024-01-07 16:00:00", bookingDTO.getCheckOut());
        assertEquals(102, bookingDTO.getRoomId());
        assertEquals(2, bookingDTO.getCustomerId());
    }
}

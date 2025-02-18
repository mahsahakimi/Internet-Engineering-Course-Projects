package edu.ut.ie.hotelreservation;

import edu.ut.ie.hotelreservation.model.Room;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RoomTest {

    private Room room;

    @BeforeEach
    void setUp() {
        room = new Room(101, 2);
    }

    @AfterEach
    void tearDown() {
        room = null;
    }

    @Test
    void testRoomCreation() {
        assertEquals(101, room.getId());
        assertEquals(2, room.getCapacity());
    }

    @Test
    void testRoomCreationWithInvalidCapacity() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Room(101, -1);
        });
    }
}

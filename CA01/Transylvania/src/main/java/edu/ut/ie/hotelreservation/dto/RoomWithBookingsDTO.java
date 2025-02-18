package edu.ut.ie.hotelreservation.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RoomWithBookingsDTO {

    private int room_id;
    private int capacity;
    private List<BookingsByRoomDTO> bookings;
}

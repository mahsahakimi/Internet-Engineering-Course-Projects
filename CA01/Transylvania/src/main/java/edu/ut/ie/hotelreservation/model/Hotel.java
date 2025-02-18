package edu.ut.ie.hotelreservation.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import edu.ut.ie.hotelreservation.dto.BookingsByRoomDTO;
import edu.ut.ie.hotelreservation.dto.RoomWithBookingsDTO;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class Hotel {

    private List<Customer> customers;
    private List<Room> rooms;
    private List<Booking> bookings;

    public Hotel() {
        this.customers = new ArrayList<>();
        this.rooms = new ArrayList<>();
        this.bookings = new ArrayList<>();
    }

    public String logState() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        return objectMapper.writeValueAsString(this.getRoomsWithBookings());
    }

    public List<RoomWithBookingsDTO> getRoomsWithBookings() {
        return rooms.stream()
                .map(room -> {
                    RoomWithBookingsDTO roomWithBookings = new RoomWithBookingsDTO();
                    roomWithBookings.setRoom_id(room.getId());
                    roomWithBookings.setCapacity(room.getCapacity());
                    roomWithBookings.setBookings(getBookingsByRoom(room.getId()));
                    return roomWithBookings;
                })
                .collect(Collectors.toList());
    }

    public List<BookingsByRoomDTO> getBookingsByRoom(int roomId) {
        return bookings.stream()
                .filter(booking -> booking.getRoom().getId() == roomId)
                .map(booking -> new BookingsByRoomDTO(booking.getId(), booking.getCustomer(), booking.getCheckInDate(), booking.getCheckOutDate()))
                .collect(Collectors.toList());
    }

    public List<Room> getRooms(int minCapacity) {
        return rooms.stream()
                .filter(room -> room.getCapacity() >= minCapacity)
                .collect(Collectors.toList());
    }

    public String getOldestCustomerName() {
        return customers.stream()
                .max((c1, c2) -> Integer.compare(c1.getAge(), c2.getAge()))
                .map(customer -> customer.getName())
                .orElse(null);
    }

    public List<String> getCustomerPhonesByRoomNumber(int roomNumber) {
        return bookings.stream()
                .filter(booking -> booking.getRoom().getId() == roomNumber)
                .map(booking -> booking.getCustomer().getPhone())
                .collect(Collectors.toList());
    }
}

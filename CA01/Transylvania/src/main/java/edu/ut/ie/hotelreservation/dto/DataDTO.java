package edu.ut.ie.hotelreservation.dto;

import edu.ut.ie.hotelreservation.model.Customer;
import edu.ut.ie.hotelreservation.model.Room;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class DataDTO {

    private List<Customer> customers;
    private List<Room> rooms;
    private List<BookingDTO> bookings;
}

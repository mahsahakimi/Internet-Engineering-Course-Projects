package edu.ut.ie.hotelreservation.dto;

import edu.ut.ie.hotelreservation.model.Customer;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookingsByRoomDTO {

    private int id;
    private Customer customer;
    private String check_in;
    private String check_out;

    public BookingsByRoomDTO(int id, Customer customer, String check_in, String check_out) {
        this.id = id;
        this.check_out = check_out;
        this.check_in = check_in;
        this.customer = customer;
    }
}

package edu.ut.ie.hotelreservation.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Getter
@Setter
@NoArgsConstructor
public class Booking {

    private int id;
    private Customer customer;
    private Room room;
    private String checkInDate;
    private String checkOutDate;

    public Booking(int id, Customer customer, Room room, String checkInDate, String checkOutDate) {
        this.id = id;
        this.customer = customer;
        this.room = room;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
    }

    public long getStayDurationInDays() {
        LocalDate checkIn = LocalDate.parse(checkInDate.split(" ")[0]);
        LocalDate checkOut = LocalDate.parse(checkOutDate.split(" ")[0]);
        return ChronoUnit.DAYS.between(checkIn, checkOut);
    }
}

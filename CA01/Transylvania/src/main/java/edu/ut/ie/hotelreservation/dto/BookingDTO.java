package edu.ut.ie.hotelreservation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BookingDTO {

    private int id;

    @JsonProperty("customer_id")
    private int customerId;

    @JsonProperty("room_id")
    private int roomId;

    @JsonProperty("check_in")
    private String checkIn;

    @JsonProperty("check_out")
    private String checkOut;

    public BookingDTO(int id, String checkIn, String checkOut, int roomId, int customerId) {
        this.id = id;
        this.checkOut = checkOut;
        this.checkIn = checkIn;
        this.roomId = roomId;
        this.customerId = customerId;
    }
}

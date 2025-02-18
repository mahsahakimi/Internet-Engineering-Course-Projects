package edu.ut.ie.hotelreservation;

import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.SerializationFeature;
import edu.ut.ie.hotelreservation.dto.DataDTO;
import edu.ut.ie.hotelreservation.model.Booking;
import edu.ut.ie.hotelreservation.model.Customer;
import edu.ut.ie.hotelreservation.model.Hotel;
import edu.ut.ie.hotelreservation.model.Room;

import java.io.File;
import java.io.IOException;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        DataDTO data = objectMapper.readValue(new File("src/main/resources/data.json"), DataDTO.class);

        Hotel hotel = new Hotel();
        hotel.setCustomers(data.getCustomers());
        hotel.setRooms(data.getRooms());

        hotel.setBookings(data.getBookings(

                        ).stream()
                .map(bookingDTO -> {
                    Customer customer = hotel.getCustomers().stream()
                            .filter(c -> c.getSsn() == bookingDTO.getCustomerId())
                            .findFirst()
                            .orElse(null);

                    Room room = hotel.getRooms().stream()
                            .filter(r -> r.getId() == bookingDTO.getRoomId())
                            .findFirst()
                            .orElse(null);

                    return new Booking(
                            bookingDTO.getId(),
                            customer,
                            room,
                            bookingDTO.getCheckIn(),
                            bookingDTO.getCheckOut()
                    );
                })
                .collect(Collectors.toList())
        );

        String state = hotel.logState();
        objectMapper.writeValue(new File("src/main/resources/state.json"), state);

//        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
//        System.out.println(state);
        System.out.println("Hotel state written to state.json");
    }
}

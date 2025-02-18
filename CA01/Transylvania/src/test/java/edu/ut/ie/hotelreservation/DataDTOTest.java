package edu.ut.ie.hotelreservation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import edu.ut.ie.hotelreservation.dto.DataDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;

public class DataDTOTest {

    private ObjectMapper objectMapper;
    private DataDTO data;

    @BeforeEach
    void setUp() throws IOException {
        objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        createSampleJson();
        data = objectMapper.readValue(new File("sample.json"), DataDTO.class);
    }

    @AfterEach
    void tearDown() {
        objectMapper = null;
        data = null;

        File file = new File("sample.json");
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    void testDataDTOLoading() {
        assertNotNull(data);
        assertNotNull(data.getCustomers());
        assertNotNull(data.getRooms());
        assertNotNull(data.getBookings());

        assertEquals(2, data.getCustomers().size());
        assertEquals(3, data.getRooms().size());
        assertEquals(2, data.getBookings().size());

        assertEquals(1, data.getCustomers().get(0).getSsn());
        assertEquals("John Doe", data.getCustomers().get(0).getName());
        assertEquals("09177020690", data.getCustomers().get(0).getPhone());
        assertEquals(30, data.getCustomers().get(0).getAge());

        assertEquals(2, data.getCustomers().get(1).getSsn());
        assertEquals("Jane Smith", data.getCustomers().get(1).getName());
        assertEquals("09106764528", data.getCustomers().get(1).getPhone());
        assertEquals(25, data.getCustomers().get(1).getAge());

        assertEquals(101, data.getRooms().get(0).getId());
        assertEquals(2, data.getRooms().get(0).getCapacity());

        assertEquals(102, data.getRooms().get(1).getId());
        assertEquals(3, data.getRooms().get(1).getCapacity());

        assertEquals(103, data.getRooms().get(2).getId());
        assertEquals(1, data.getRooms().get(2).getCapacity());

        assertEquals(1, data.getBookings().get(0).getId());
        assertEquals(1, data.getBookings().get(0).getCustomerId());
        assertEquals(101, data.getBookings().get(0).getRoomId());
        assertEquals("2024-01-01 08:00:00", data.getBookings().get(0).getCheckIn());
        assertEquals("2024-01-03 16:00:00", data.getBookings().get(0).getCheckOut());

        assertEquals(2, data.getBookings().get(1).getId());
        assertEquals(2, data.getBookings().get(1).getCustomerId());
        assertEquals(102, data.getBookings().get(1).getRoomId());
        assertEquals("2024-01-05 09:00:00", data.getBookings().get(1).getCheckIn());
        assertEquals("2024-01-07 16:00:00", data.getBookings().get(1).getCheckOut());
    }

    // Helper method to create a sample data.json file by name sample.json
    private void createSampleJson() throws IOException {
        String json = "{\n" +
                "  \"customers\": [\n" +
                "    {\"ssn\": 1, \"name\": \"John Doe\", \"phone\": \"09177020690\", \"age\": 30},\n" +
                "    {\"ssn\": 2, \"name\": \"Jane Smith\", \"phone\": \"09106764528\", \"age\": 25}\n" +
                "  ],\n" +
                "  \"rooms\": [\n" +
                "    {\"id\": 101, \"capacity\": 2},\n" +
                "    {\"id\": 102, \"capacity\": 3},\n" +
                "    {\"id\": 103, \"capacity\": 1}\n" +
                "  ],\n" +
                "  \"bookings\": [\n" +
                "    {\"id\": 1, \"customer_id\": 1, \"room_id\": 101, \"check_in\": \"2024-01-01 08:00:00\", \"check_out\": \"2024-01-03 16:00:00\"},\n" +
                "    {\"id\": 2, \"customer_id\": 2, \"room_id\": 102, \"check_in\": \"2024-01-05 09:00:00\", \"check_out\": \"2024-01-07 16:00:00\"}\n" +
                "  ]\n" +
                "}";

        File file = new File("sample.json");
        objectMapper.writeValue(file, objectMapper.readTree(json));
    }
}

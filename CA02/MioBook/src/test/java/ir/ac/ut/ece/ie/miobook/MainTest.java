package ir.ac.ut.ece.ie.miobook;

import ir.ac.ut.ece.ie.miobook.database.UserDatabase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.*;

public class MainTest {

    @AfterEach
    public void tearDown() {
        UserDatabase database = UserDatabase.getInstance();
        database.getUsers().clear();
    }

    @Test
    public void testAddUserSuccess() throws IOException {
        String input = "add_user {\"role\": \"customer\", \"username\": \"test-name\", \"password\": \"1234\", \"email\": \"my.mail@mail.com\", \"address\": {\"country\": \"Iran\", \"city\": \"Karaj\"}}";
//        add_user {"role": "customer", "username": "test-name", "password": "1234", "email": "my.mail@mail.com", "address": {"country": "Iran", "city": "Karaj"}}
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        Main.main(new String[]{});

        String expectedOutput = "{\"success\": true, \"message\": \"User added successfully.\"}\n";
        assertEquals(expectedOutput, outContent.toString());
    }
}

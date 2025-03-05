package ir.ac.ut.ece.ie.miobook;

import ir.ac.ut.ece.ie.miobook.database.UserDatabase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    @Test
    public void testAddCreditSuccess() throws IOException {
        String addUserInput = "add_user {\"role\": \"customer\", \"username\": \"test-name\", \"password\": \"1234\", \"email\": \"my.mail@mail.com\", \"address\": {\"country\": \"Iran\", \"city\": \"Karaj\"}}";
        InputStream addUserIn = new ByteArrayInputStream(addUserInput.getBytes());
        System.setIn(addUserIn);

        ByteArrayOutputStream addUserOutContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(addUserOutContent));

        Main.main(new String[]{});

        String addCreditInput = "add_credit {\"username\": \"test-name\", \"credit\": 8200}";
//        add_credit {"username": "test-name", "credit": 8200}
        InputStream addCreditIn = new ByteArrayInputStream(addCreditInput.getBytes());
        System.setIn(addCreditIn);

        ByteArrayOutputStream addCreditOutContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(addCreditOutContent));

        Main.main(new String[]{});

        String expectedOutput = "{\"success\": true, \"message\": \"Credit added successfully.\"}\n";
        assertEquals(expectedOutput, addCreditOutContent.toString());
    }

    @Test
    public void testShowUserDetailsSuccess() throws IOException {
        String addUserInput = "add_user {\"role\": \"customer\", \"username\": \"test-name\", \"password\": \"1234\", \"email\": \"my.mail@mail.com\", \"address\": {\"country\": \"Iran\", \"city\": \"Karaj\"}}";
        InputStream addUserIn = new ByteArrayInputStream(addUserInput.getBytes());
        System.setIn(addUserIn);

        ByteArrayOutputStream addUserOutContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(addUserOutContent));

        Main.main(new String[]{});

        String addCreditInput = "add_credit {\"username\": \"test-name\", \"credit\": 2000}";
        InputStream addCreditIn = new ByteArrayInputStream(addCreditInput.getBytes());
        System.setIn(addCreditIn);

        ByteArrayOutputStream addCreditOutContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(addCreditOutContent));

        Main.main(new String[]{});

        String showDetailsInput = "show_user_details {\"username\": \"test-name\"}";
//        show_user_details {"username": "test-name"}
        InputStream showDetailsIn = new ByteArrayInputStream(showDetailsInput.getBytes());
        System.setIn(showDetailsIn);

        ByteArrayOutputStream showDetailsOutContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(showDetailsOutContent));

        Main.main(new String[]{});

        String output = showDetailsOutContent.toString();
        assertTrue(output.contains("\"success\":true"));
        assertTrue(output.contains("\"message\":\"User details retrieved successfully.\""));
        assertTrue(output.contains("\"username\":\"test-name\""));
        assertTrue(output.contains("\"role\":\"customer\""));
        assertTrue(output.contains("\"email\":\"my.mail@mail.com\""));
        assertTrue(output.contains("\"country\":\"Iran\""));
        assertTrue(output.contains("\"city\":\"Karaj\""));
        assertTrue(output.contains("\"balance\":2000"));
    }
}

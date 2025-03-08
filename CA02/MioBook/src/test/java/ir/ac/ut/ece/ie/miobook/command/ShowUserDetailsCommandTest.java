package ir.ac.ut.ece.ie.miobook.command;

import ir.ac.ut.ece.ie.miobook.database.UserDatabase;
import ir.ac.ut.ece.ie.miobook.model.Address;
import ir.ac.ut.ece.ie.miobook.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ShowUserDetailsCommandTest {

    private UserDatabase database;
    private User user;

    @BeforeEach
    public void setUp() {
        database = UserDatabase.getInstance();
        Address address = new Address("USA", "New York");
        user = new User("customer", "test-name", "1234", "test@mail.com", address);
        user.setBalance(2000);
        database.addUser(user);
    }

    @AfterEach
    public void tearDown() {
        database.getUsers().clear();
    }

    @Test
    public void testShowUserDetailsSuccess() throws IOException {
        String inputJson = "{\"username\": \"test-name\"}";

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        ShowUserDetailsCommand.execute(inputJson);

        String output = outContent.toString();
        assertTrue(output.contains("\"success\":true"));
        assertTrue(output.contains("\"message\":\"User details retrieved successfully.\""));
        assertTrue(output.contains("\"username\":\"test-name\""));
        assertTrue(output.contains("\"role\":\"customer\""));
        assertTrue(output.contains("\"email\":\"test@mail.com\""));
        assertTrue(output.contains("\"country\":\"USA\""));
        assertTrue(output.contains("\"city\":\"New York\""));
        assertTrue(output.contains("\"balance\":2000"));
    }

    @Test
    public void testShowUserDetailsAdminUser() throws IOException {
        Address address = new Address("USA", "New York");
        User adminUser = new User("admin", "admin-name", "1234", "admin@mail.com", address);
        database.addUser(adminUser);

        String inputJson = "{\"username\": \"admin-name\"}";

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        ShowUserDetailsCommand.execute(inputJson);

        String output = outContent.toString();
        assertTrue(output.contains("\"success\":true"));
        assertTrue(output.contains("\"message\":\"User details retrieved successfully.\""));
        assertTrue(output.contains("\"username\":\"admin-name\""));
        assertTrue(output.contains("\"role\":\"admin\""));
        assertTrue(output.contains("\"email\":\"admin@mail.com\""));
        assertTrue(output.contains("\"country\":\"USA\""));
        assertTrue(output.contains("\"city\":\"New York\""));
        assertTrue(!output.contains("\"balance\""));
    }
}

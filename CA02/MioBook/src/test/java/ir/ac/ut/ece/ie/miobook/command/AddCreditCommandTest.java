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

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AddCreditCommandTest {

    private UserDatabase database;
    private User user;

    @BeforeEach
    public void setUp() {
        database = UserDatabase.getInstance();
        Address address = new Address("USA", "New York");
        user = new User("customer", "test-name", "1234", "test@mail.com", address);
        database.addUser(user);
    }

    @AfterEach
    public void tearDown() {
        database.getUsers().clear();
    }

    @Test
    public void testAddCreditSuccess() throws IOException {
        String inputJson = "{\"username\": \"test-name\", \"credit\": 8200}";

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        AddCreditCommand.execute(inputJson);

        String expectedOutput = "{\"success\": true, \"message\": \"Credit added successfully.\"}\n";
        assertEquals(expectedOutput, outContent.toString());

        assertEquals(8200, user.getBalance());
    }

    @Test
    public void testAddCreditInvalidCredit() throws IOException {
        String inputJson = "{\"username\": \"test-name\", \"credit\": 0}";

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        AddCreditCommand.execute(inputJson);

        String expectedOutput = "{\"success\": false, \"message\": \"Credit must be greater than zero.\"}\n";
        assertEquals(expectedOutput, outContent.toString());

        assertEquals(0, user.getBalance());
    }

    @Test
    public void testAddCreditLessThanMinimum() throws IOException {
        String inputJson = "{\"username\": \"test-name\", \"credit\": 99}";

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        AddCreditCommand.execute(inputJson);

        String expectedOutput = "{\"success\": false, \"message\": \"Minimum credit is 100 cents (1 dollar).\"}\n";
        assertEquals(expectedOutput, outContent.toString());

        assertEquals(0, user.getBalance());
    }

    @Test
    public void testAddCreditUserNotFound() throws IOException {
        String inputJson = "{\"username\": \"not-found\", \"credit\": 8200}";

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        AddCreditCommand.execute(inputJson);

        String expectedOutput = "{\"success\": false, \"message\": \"User not found.\"}\n";
        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    public void testAddCreditNotCustomer() throws IOException {
        Address address = new Address("USA", "New York");
        User adminUser = new User("admin", "admin-name", "1234", "admin@mail.com", address);
        database.addUser(adminUser);

        String inputJson = "{\"username\": \"admin-name\", \"credit\": 8200}";

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        AddCreditCommand.execute(inputJson);

        String expectedOutput = "{\"success\": false, \"message\": \"This command is only for customer users.\"}\n";
        assertEquals(expectedOutput, outContent.toString());
    }
}

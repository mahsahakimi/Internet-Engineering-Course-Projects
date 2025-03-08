package ir.ac.ut.ece.ie.miobook.command;

import ir.ac.ut.ece.ie.miobook.database.UserDatabase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AddUserCommandTest {

    @AfterEach
    public void tearDown() {
        UserDatabase database = UserDatabase.getInstance();
        database.getUsers().clear();
    }

    @Test
    public void testAddUserSuccess() throws IOException {
        String inputJson = "{\"role\": \"customer\", \"username\": \"test-name\", \"password\": \"1234\", \"email\": \"my.mail@mail.com\", \"address\": {\"country\": \"Iran\", \"city\": \"Karaj\"}}";


        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        AddUserCommand.execute(inputJson);

        String expectedOutput = "{\"success\": true, \"message\": \"User added successfully.\"}\n";
        assertEquals(expectedOutput, outContent.toString());
    }
}

package ir.ac.ut.ece.ie.miobook.command;

import ir.ac.ut.ece.ie.miobook.database.BookDatabase;
import ir.ac.ut.ece.ie.miobook.database.UserDatabase;
import ir.ac.ut.ece.ie.miobook.model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AddCartCommandTest {

    private UserDatabase userDatabase;
    private BookDatabase bookDatabase;
    private User normalUser;
    private Book book;

    @BeforeEach
    public void setUp() {
        userDatabase = UserDatabase.getInstance();
        bookDatabase = BookDatabase.getInstance();

        Address address = new Address("USA", "New York");
        normalUser = new User("customer", "normal_user", "1234", "normal@mail.com", address);
        userDatabase.addUser(normalUser);

        Author author = new Author(
                "Author Name",
                "Pen Name",
                "Nationality",
                LocalDate.of(1900, 1, 1),
                LocalDate.of(1980, 1, 1)
        );

        List<String> genres = new ArrayList<>();
        genres.add("Fantasy");
        book = new Book("The Magical Adventure", author, "Publisher", 2023, genres, 19.99, "Synopsis", "Content");
        bookDatabase.addBook(book);
    }

    @AfterEach
    public void tearDown() {
        userDatabase.getUsers().clear();
        bookDatabase.getBooks().clear();
    }

    @Test
    public void testAddCartSuccess() throws IOException {
        String inputJson = "{\"username\": \"normal_user\", \"title\": \"The Magical Adventure\"}";

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        AddCartCommand.execute(inputJson);

        String expectedOutput = "{\"success\": true, \"message\": \"Added book to cart.\"}\n";
        assertEquals(expectedOutput, outContent.toString());
        assertEquals(1, normalUser.getCart().size());
    }

    @Test
    public void testAddCartUserNotFound() throws IOException {
        String inputJson = "{\"username\": \"nonexistent_user\", \"title\": \"The Magical Adventure\"}";

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        AddCartCommand.execute(inputJson);

        String expectedOutput = "{\"success\": false, \"message\": \"User not found.\"}\n";
        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    public void testAddCartBookNotFound() throws IOException {
        String inputJson = "{\"username\": \"normal_user\", \"title\": \"Nonexistent Book\"}";

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        AddCartCommand.execute(inputJson);

        String expectedOutput = "{\"success\": false, \"message\": \"Book not found.\"}\n";
        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    public void testAddCartAdminUser() throws IOException {
        Address address = new Address("USA", "New York");
        User adminUser = new User("admin", "admin_user", "1234", "admin@mail.com", address);
        adminUser.setRole(Role.admin);
        userDatabase.addUser(adminUser);

        String inputJson = "{\"username\": \"admin_user\", \"title\": \"The Magical Adventure\"}";

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        AddCartCommand.execute(inputJson);

        String expectedOutput = "{\"success\": false, \"message\": \"Permission denied. Admin cannot add to cart.\"}\n";
        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    public void testAddCartCartFull() throws IOException {
        for (int i = 0; i < 10; i++) {
            normalUser.getCart().add(book);
        }

        String inputJson = "{\"username\": \"normal_user\", \"title\": \"The Magical Adventure\"}";

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        AddCartCommand.execute(inputJson);

        String expectedOutput = "{\"success\": false, \"message\": \"Cart is full.\"}\n";
        assertEquals(expectedOutput, outContent.toString());
    }
}

package ir.ac.ut.ece.ie.miobook.command;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ShowPurchasedBooksCommandTest {

    private UserDatabase userDatabase;
    private BookDatabase bookDatabase;
    private User normalUser;
    private Book book1;
    private Book book2;

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

        book1 = new Book("The Magical Adventure", author, "Publisher", 2023, genres, 1000, "Synopsis", "Content");
        book2 = new Book("Another Adventure", author, "Publisher", 2023, genres, 2000, "Synopsis", "Content");

        bookDatabase.addBook(book1);
        bookDatabase.addBook(book2);
    }

    @AfterEach
    public void tearDown() {
        userDatabase.getUsers().clear();
        bookDatabase.getBooks().clear();
    }

    @Test
    public void testShowPurchasedBooksSuccess() throws IOException {
        normalUser.getOwnedBooks().add(book1);
        normalUser.getBorrowedBooks().put(book2, LocalDate.now().plusDays(7));

        String inputJson = "{\"username\": \"normal_user\"}";

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        ShowPurchasedBooksCommand.execute(inputJson);

        String actualOutput = outContent.toString();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualJson = mapper.readTree(actualOutput);
        JsonNode expectedJson = mapper.readTree("{\"success\":true,\"message\":\"Purchased books retrieved successfully.\",\"data\":{\"username\":\"normal_user\",\"books\":[{\"title\":\"The Magical Adventure\",\"author\":\"Author Name\",\"publisher\":\"Publisher\",\"category\":[\"Fantasy\"],\"year\":2023,\"price\":1000,\"isBorrowed\":false},{\"title\":\"Another Adventure\",\"author\":\"Author Name\",\"publisher\":\"Publisher\",\"category\":[\"Fantasy\"],\"year\":2023,\"price\":2000,\"isBorrowed\":true}]}}");

        assertEquals(expectedJson.get("success"), actualJson.get("success"));
        assertEquals(expectedJson.get("message"), actualJson.get("message"));
        assertEquals(expectedJson.get("data").get("username"), actualJson.get("data").get("username"));

        JsonNode expectedBooks = expectedJson.get("data").get("books");
        JsonNode actualBooks = actualJson.get("data").get("books");

        assertEquals(expectedBooks.size(), actualBooks.size());

        for (JsonNode expectedBook : expectedBooks) {
            boolean found = false;
            for (JsonNode actualBook : actualBooks) {
                if (expectedBook.equals(actualBook)) {
                    found = true;
                    break;
                }
            }
            assertTrue(found);
        }
    }

    @Test
    public void testShowPurchasedBooksUserNotFound() throws IOException {
        String inputJson = "{\"username\": \"nonexistent_user\"}";

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        ShowPurchasedBooksCommand.execute(inputJson);

        String expectedOutput = "{\"success\": false, \"message\": \"User not found.\"}\n";
        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    public void testShowPurchasedBooksEmpty() throws IOException {
        String inputJson = "{\"username\": \"normal_user\"}";

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        ShowPurchasedBooksCommand.execute(inputJson);

        String actualOutput = outContent.toString();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualJson = mapper.readTree(actualOutput);
        JsonNode expectedJson = mapper.readTree("{\"success\":true,\"message\":\"Purchased books retrieved successfully.\",\"data\":{\"username\":\"normal_user\",\"books\":[]}}");

        assertEquals(expectedJson.get("success"), actualJson.get("success"));
        assertEquals(expectedJson.get("message"), actualJson.get("message"));
        assertEquals(expectedJson.get("data").get("username"), actualJson.get("data").get("username"));
        assertEquals(expectedJson.get("data").get("books"), actualJson.get("data").get("books")); // Compare the books array directly
    }

    @Test
    public void testShowPurchasedBooksAdmin() throws IOException {
        Address address = new Address("USA", "New York");
        User adminUser = new User("admin", "admin_user", "1234", "admin@mail.com", address);
        adminUser.setRole(Role.admin);
        userDatabase.addUser(adminUser);

        String inputJson = "{\"username\": \"admin_user\"}";

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        ShowPurchasedBooksCommand.execute(inputJson);

        String expectedOutput = "{\"success\": false, \"message\": \"Admins do not have purchased books.\"}\n";
        assertEquals(expectedOutput, outContent.toString());
    }
}

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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PurchaseCartCommandTest {

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
        normalUser.setBalance(5000);
        userDatabase.addUser(normalUser);

        Author author = new Author(
                "Author Name",
                "Pen Name",
                "Nationality",
                MySimpleDate.of(1900, 1, 1),
                MySimpleDate.of(1980, 1, 1)
        );

        List<String> genres = new ArrayList<>();
        genres.add("Fantasy");

        book1 = new Book("The Magical Adventure", author, "Publisher", 2023, genres, 1999, "Synopsis", "Content");
        book2 = new Book("Another Adventure", author, "Publisher", 2023, genres, 2201, "Synopsis", "Content");

        bookDatabase.addBook(book1);
        bookDatabase.addBook(book2);
    }

    @AfterEach
    public void tearDown() {
        userDatabase.getUsers().clear();
        bookDatabase.getBooks().clear();
    }

    @Test
    public void testPurchaseCartSuccess() throws IOException {
        normalUser.getCart().add(new CartElement(book1, 0));
        normalUser.getCart().add(new CartElement(book2, 0));

        String inputJson = "{\"username\": \"normal_user\"}";

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        PurchaseCartCommand.execute(inputJson);

        int totalCost = book1.getPrice() + book2.getPrice();

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = now.format(formatter);

        String expectedOutput = "{\"success\": true, \"message\": \"Purchase completed successfully.\", \"data\": {\"bookCount\": 2, \"totalCost\": " + totalCost + ", \"date\": \"" + formattedDateTime + "\"}}\n";
        assertEquals(expectedOutput, outContent.toString());
        assertEquals(5000 - totalCost, normalUser.getBalance());
        assertTrue(normalUser.getCart().isEmpty());
        assertEquals(1, normalUser.getPurchases().size());
        assertEquals(totalCost, normalUser.getPurchases().get(0).getTotalCost());
    }

    @Test
    public void testPurchaseCartUserNotFound() throws IOException {
        String inputJson = "{\"username\": \"nonexistent_user\"}";

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        PurchaseCartCommand.execute(inputJson);

        String expectedOutput = "{\"success\": false, \"message\": \"User not found.\"}\n";
        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    public void testPurchaseCartEmptyCart() throws IOException {
        String inputJson = "{\"username\": \"normal_user\"}";

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        PurchaseCartCommand.execute(inputJson);

        String expectedOutput = "{\"success\": false, \"message\": \"Cart is empty.\"}\n";
        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    public void testPurchaseCartInsufficientBalance() throws IOException {
        normalUser.getCart().add(new CartElement(book1, 0));
        normalUser.getCart().add(new CartElement(book2, 0));
        normalUser.setBalance(1000);

        String inputJson = "{\"username\": \"normal_user\"}";

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        PurchaseCartCommand.execute(inputJson);

        String expectedOutput = "{\"success\": false, \"message\": \"Insufficient balance.\"}\n";
        assertEquals(expectedOutput, outContent.toString());
    }
}

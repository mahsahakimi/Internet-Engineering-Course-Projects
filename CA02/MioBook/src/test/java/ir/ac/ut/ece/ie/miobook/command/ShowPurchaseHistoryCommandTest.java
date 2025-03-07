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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ShowPurchaseHistoryCommandTest {

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
                MySimpleDate.of(1900, 1, 1),
                MySimpleDate.of(1980, 1, 1)
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

//    @Test
//    public void testShowPurchaseHistorySuccess() throws IOException {
//        List<String> bookTitles = new ArrayList<>();
//        bookTitles.add(book1.getTitle());
//        bookTitles.add(book2.getTitle());
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        String purchaseTime = LocalDateTime.now().format(formatter);
//        Purchase purchase = new Purchase(bookTitles, 3000, purchaseTime);
//
//        normalUser.getPurchases().add(purchase);
//        normalUser.getBorrowedBooks().put(book1, LocalDateTime.now().plusDays(2).toLocalDate());
//
//        String inputJson = "{\"username\": \"normal_user\"}";
//
//        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
//        System.setOut(new PrintStream(outContent));
//
//        ShowPurchaseHistoryCommand.execute(inputJson);
//
//        String actualOutput = outContent.toString();
//
//        ObjectMapper mapper = new ObjectMapper();
//        JsonNode actualJson = mapper.readTree(actualOutput);
//        JsonNode expectedJson = mapper.readTree("{\"success\":true,\"message\":\"Purchase history retrieved successfully.\",\"data\":{\"username\":\"normal_user\",\"purchaseHistory\":[{\"purchaseDate\":\"" + purchaseTime + "\",\"items\":[{\"title\":\"The Magical Adventure\",\"author\":\"Author Name\",\"publisher\":\"Publisher\",\"genres\":[\"Fantasy\"],\"year\":2023,\"price\":1000,\"isBorrowed\":true,\"borrowDays\":2,\"finalPrice\":200},{\"title\":\"Another Adventure\",\"author\":\"Author Name\",\"publisher\":\"Publisher\",\"genres\":[\"Fantasy\"],\"year\":2023,\"price\":2000,\"isBorrowed\":false,\"borrowDays\":0,\"finalPrice\":2000}],\"totalCost\":3000}]}}");
//
//        assertEquals(expectedJson.get("success"), actualJson.get("success"));
//        assertEquals(expectedJson.get("message"), actualJson.get("message"));
//        assertEquals(expectedJson.get("data").get("username"), actualJson.get("data").get("username"));
//
//        JsonNode expectedPurchaseHistory = expectedJson.get("data").get("purchaseHistory");
//        JsonNode actualPurchaseHistory = actualJson.get("data").get("purchaseHistory");
//
//        assertEquals(expectedPurchaseHistory.size(), actualPurchaseHistory.size());
//
//        for (int i = 0; i < expectedPurchaseHistory.size(); i++) {
//            JsonNode expectedPurchase = expectedPurchaseHistory.get(i);
//            JsonNode actualPurchase = actualPurchaseHistory.get(i);
//
//            assertEquals(expectedPurchase.get("purchaseDate"), actualPurchase.get("purchaseDate"));
//            assertEquals(expectedPurchase.get("totalCost"), actualPurchase.get("totalCost"));
//
//            JsonNode expectedItems = expectedPurchase.get("items");
//            JsonNode actualItems = actualPurchase.get("items");
//
//            assertEquals(expectedItems.size(), actualItems.size());
//            for (JsonNode expectedItem : expectedItems) {
//                boolean found = false;
//                for (JsonNode actualItem : actualItems) {
//                    if (expectedItem.equals(actualItem)) {
//                        found = true;
//                        break;
//                    }
//                }
//                assertTrue(found);
//            }
//        }
//    }

    @Test
    public void testShowPurchaseHistoryUserNotFound() throws IOException {
        String inputJson = "{\"username\": \"nonexistent_user\"}";

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        ShowPurchaseHistoryCommand.execute(inputJson);

        String expectedOutput = "{\"success\": false, \"message\": \"User not found.\"}\n";
        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    public void testShowPurchaseHistoryEmpty() throws IOException {
        String inputJson = "{\"username\": \"normal_user\"}";

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        ShowPurchaseHistoryCommand.execute(inputJson);

        String actualOutput = outContent.toString();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualJson = mapper.readTree(actualOutput);
        JsonNode expectedJson = mapper.readTree("{\"success\":true,\"message\":\"Purchase history retrieved successfully.\",\"data\":{\"username\":\"normal_user\",\"purchaseHistory\":[]}}");

        assertEquals(expectedJson.get("success"), actualJson.get("success"));
        assertEquals(expectedJson.get("message"), actualJson.get("message"));
        assertEquals(expectedJson.get("data").get("username"), actualJson.get("data").get("username"));
        assertEquals(expectedJson.get("data").get("purchaseHistory"), actualJson.get("data").get("purchaseHistory"));
    }

    @Test
    public void testShowPurchaseHistoryAdmin() throws IOException {
        Address address = new Address("USA", "New York");
        User adminUser = new User("admin", "admin_user", "1234", "admin@mail.com", address);
        adminUser.setRole(Role.admin);
        userDatabase.addUser(adminUser);

        String inputJson = "{\"username\": \"admin_user\"}";

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        ShowPurchaseHistoryCommand.execute(inputJson);

        String expectedOutput = "{\"success\": false, \"message\": \"Admins do not have a purchase history.\"}\n";
        assertEquals(expectedOutput, outContent.toString());
    }
}

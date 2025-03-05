package ir.ac.ut.ece.ie.miobook;

import ir.ac.ut.ece.ie.miobook.database.AuthorDatabase;
import ir.ac.ut.ece.ie.miobook.database.BookDatabase;
import ir.ac.ut.ece.ie.miobook.database.UserDatabase;
import ir.ac.ut.ece.ie.miobook.model.Address;
import ir.ac.ut.ece.ie.miobook.model.Author;
import ir.ac.ut.ece.ie.miobook.model.Role;
import ir.ac.ut.ece.ie.miobook.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.*;
import java.time.LocalDate;

public class MainTest {

    private UserDatabase userDatabase;
    private BookDatabase bookDatabase;
    private AuthorDatabase authorDatabase;
    private User adminUser;
    private Author author;

    @BeforeEach
    public void setUp() {
        userDatabase = UserDatabase.getInstance();
        bookDatabase = BookDatabase.getInstance();
        authorDatabase = AuthorDatabase.getInstance();
        Address address = new Address("USA", "New York");
        adminUser = new User("admin", "admin_user", "1234", "admin@mail.com", address);
        adminUser.setRole(Role.admin);
        userDatabase.addUser(adminUser);
        author = new Author(
                "sample name",
                "Pen Name",
                "Nationality",
                LocalDate.of(1900, 1, 1),
                LocalDate.of(1980, 1, 1)
        );
        authorDatabase.addAuthor(author);
    }

    @AfterEach
    public void tearDown() {
        userDatabase.getUsers().clear();
        bookDatabase.getBooks().clear();
        authorDatabase.getAuthors().clear();
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

    @Test
    public void testAddBookSuccess() throws IOException {
        String input = "add_book {\"username\": \"admin_user\", \"title\": \"sample book\", \"author\": \"sample name\", \"publisher\": \"name\", \"year\": 2012, \"price\": 250, \"synopsis\": \"lorem\", \"content\": \"lorem ipsum\", \"genres\": [\"horror\", \"thriller\"]}";
//        add_book {"username": "admin_user", "title": "sample book", "author": "sample name", "publisher": "name", "year": 2012, "price": 250, "synopsis": "lorem", "content": "lorem ipsum", "genres": ["horror", "thriller"]}
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        Main.main(new String[]{});

        String expectedOutput = "{\"success\": true, \"message\": \"Book added successfully.\"}\n";
        assertEquals(expectedOutput, outContent.toString());
        assertEquals(1, bookDatabase.getBooks().size());
    }
}

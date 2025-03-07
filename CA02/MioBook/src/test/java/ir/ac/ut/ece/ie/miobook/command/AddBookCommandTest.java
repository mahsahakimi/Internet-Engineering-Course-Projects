package ir.ac.ut.ece.ie.miobook.command;

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
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AddBookCommandTest {

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
    public void testAddBookSuccess() throws IOException {
        String inputJson = "{\"username\": \"admin_user\", \"title\": \"sample book\", \"author\": \"sample name\", \"publisher\": \"name\", \"year\": 2012, \"price\": 250, \"synopsis\": \"lorem\", \"content\": \"lorem ipsum\", \"genres\": [\"horror\", \"thriller\"]}";

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        AddBookCommand.execute(inputJson);

        String expectedOutput = "{\"success\": true, \"message\": \"Book added successfully.\"}\n";
        assertEquals(expectedOutput, outContent.toString());

        assertEquals(1, bookDatabase.getBooks().size());
    }

    @Test
    public void testAddBookUserNotAdmin() throws IOException {
        Address address = new Address("USA", "New York");
        User normalUser = new User("customer", "normal_user", "1234", "normal@mail.com", address);
        userDatabase.addUser(normalUser);

        String inputJson = "{\"username\": \"normal_user\", \"title\": \"sample book\", \"author\": \"sample name\", \"publisher\": \"name\", \"year\": 2012, \"price\": 250, \"synopsis\": \"lorem\", \"content\": \"lorem ipsum\", \"genres\": [\"horror\", \"thriller\"]}";

        assertThrows(IllegalArgumentException.class, () -> {
            AddBookCommand.execute(inputJson);
        });

        assertEquals(0, bookDatabase.getBooks().size());
    }

    @Test
    public void testAddBookAuthorNotFound() {
        String inputJson = "{\"username\": \"admin_user\", \"title\": \"sample book\", \"author\": \"not found author\", \"publisher\": \"name\", \"year\": 2012, \"price\": 250, \"synopsis\": \"lorem\", \"content\": \"lorem ipsum\", \"genres\": [\"horror\", \"thriller\"]}";

        assertThrows(IllegalArgumentException.class, () -> {
            AddBookCommand.execute(inputJson);
        });
        assertEquals(0, bookDatabase.getBooks().size());
    }
}
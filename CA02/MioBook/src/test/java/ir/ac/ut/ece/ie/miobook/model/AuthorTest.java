package ir.ac.ut.ece.ie.miobook.model;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.DateTimeException;
import java.time.format.DateTimeParseException;

class AuthorTest {

    @Test
    void testValidAuthorCreation() {
        Author author = new Author("John Doe", "JD", "American", "1950-05-20", "2000-10-15");
        assertNotNull(author);
    }

    @Test
    void testAuthorWithNullDiedDate() {
        Author author = new Author("Jane Doe", "JD", "British", "1960-06-15", null);
        assertNotNull(author);
    }

    @Test
    void testAuthorWithEmptyDiedDate() {
        Exception exception = assertThrows(DateTimeException.class, () ->
                new Author("Jane Doe", "JD", "British", "1960-06-15", "")
        );
        assertEquals("died date is not valid...", exception.getMessage());
    }

    @Test
    void testInvalidBornDateFormat() {
        Exception exception = assertThrows(DateTimeParseException.class, () ->
                new Author("Alice", "A", "Canadian", "invalid-date", "2000-10-15")
        );
        assertTrue(exception.getMessage().contains("born date is not valid..."));
    }

    @Test
    void testInvalidDiedDateFormat() {
        Exception exception = assertThrows(DateTimeParseException.class, () ->
                new Author("Bob", "B", "German", "1970-07-20", "invalid-date")
        );
        assertTrue(exception.getMessage().contains("died date is not valid..."));
    }

    @Test
    void testBornDateAfterDiedDate() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new Author("Eve", "E", "French", "2020-05-20", "2000-10-15")
        );
        assertEquals("Invalid dates!", exception.getMessage());
    }

    @Test
    void testBornDateEqualsDiedDate() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new Author("Tom", "T", "Italian", "1990-01-01", "1990-01-01")
        );
        assertEquals("Invalid dates!", exception.getMessage());
    }

    @Test
    void testNullBornDate() {
        Exception exception = assertThrows(DateTimeException.class, () ->
                new Author("Chris", "C", "Spanish", null, "2000-10-15")
        );
        assertEquals("born date is not valid...", exception.getMessage());
    }
}

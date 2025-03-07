package ir.ac.ut.ece.ie.miobook.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;

class AuthorTest {

    @Test
    void testValidAuthor() {
        MySimpleDate born = MySimpleDate.of(1980, 5, 15);
        MySimpleDate died = MySimpleDate.of(2020, 8, 10);

        Author author = new Author("John Doe", "J.D.", "American", born, died);

        assertNotNull(author);
        assertEquals("John Doe", author.getName());
    }

    @Test
    void testAuthorWithInvalidBornAndDied() {
        MySimpleDate born = MySimpleDate.of(1980, 5, 15);
        MySimpleDate died = MySimpleDate.of(1975, 3, 10); // Born after died

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Author("Jane Doe", "J.D.", "Canadian", born, died);
        });

        assertEquals("Invalid dates!", exception.getMessage());
    }

    @Test
    void testAuthorWithNullDied() {
        MySimpleDate born = MySimpleDate.of(1985, 7, 20);
        MySimpleDate died = null; // Null died date

        Author author = new Author("Sam Smith", "S.S.", "British", born, died);

        assertNotNull(author);
        assertEquals("Sam Smith", author.getName());
    }

    @Test
    void testAuthorWithInvalidDateFormat() {
        // Invalid date format, should throw exception on validation
        MySimpleDate born = MySimpleDate.of(2025, 1, 1);
        MySimpleDate died = MySimpleDate.of(2023, 1, 1); // Born after the current date (invalid for a future-born author)

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Author("Future Author", "F.A.", "Global", born, died);
        });

        assertEquals("Invalid dates!", exception.getMessage());
    }
}
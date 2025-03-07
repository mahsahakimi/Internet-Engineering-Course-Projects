package ir.ac.ut.ece.ie.miobook.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

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
    void testAuthorWithNullDied() {
        MySimpleDate born = MySimpleDate.of(1985, 7, 20);
        MySimpleDate died = null; // Null died date

        Author author = new Author("Sam Smith", "S.S.", "British", born, died);

        assertNotNull(author);
        assertEquals("Sam Smith", author.getName());
    }
}
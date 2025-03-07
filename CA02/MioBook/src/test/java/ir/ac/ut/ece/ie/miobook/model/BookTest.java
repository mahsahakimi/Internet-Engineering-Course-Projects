package ir.ac.ut.ece.ie.miobook.model;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BookTest {

    @Test
    public void testBookCreationSuccess() {
        Author author = new Author(
                "Author Name",
                "Pen Name",
                "Nationality",
                MySimpleDate.of(1900, 1, 1),
                MySimpleDate.of(1980, 1, 1)
        );
        List<String> genres = new ArrayList<>();
        genres.add("Fantasy");
        genres.add("Adventure");

        Book book = new Book(
                "The Magical Adventure",
                author,
                "Publishing House",
                2023,
                genres,
                1999,
                "A thrilling tale of magic and friendship.",
                "Chapter 1..."
        );

        assertEquals("The Magical Adventure", book.getTitle());
        assertEquals(author, book.getAuthor());
        assertEquals("Publishing House", book.getPublisher());
        assertEquals(2023, book.getYear());
        assertEquals(genres, book.getGenres());
        assertEquals(1999, book.getPrice());
        assertEquals("A thrilling tale of magic and friendship.", book.getSynopsis());
        assertEquals("Chapter 1...", book.getContent());
    }

    @Test
    public void testBookCreationPriceInCents() {
        Author author = new Author(
                "Author Name",
                "Pen Name",
                "Nationality",
                MySimpleDate.of(1900, 1, 1),
                MySimpleDate.of(1980, 1, 1)
        );
        List<String> genres = new ArrayList<>();
        genres.add("Fantasy");

        Book book = new Book(
                "The Magical Adventure",
                author,
                "Publishing House",
                2023,
                genres,
                1999,
                "Synopsis",
                "Content"
        );

        assertEquals(1999, book.getPrice());
    }
}

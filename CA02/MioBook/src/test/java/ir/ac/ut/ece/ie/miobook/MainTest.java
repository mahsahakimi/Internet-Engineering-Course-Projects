package ir.ac.ut.ece.ie.miobook;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.ac.ut.ece.ie.miobook.database.AuthorDatabase;
import ir.ac.ut.ece.ie.miobook.database.BookDatabase;
import ir.ac.ut.ece.ie.miobook.database.ReviewDatabase;
import ir.ac.ut.ece.ie.miobook.database.UserDatabase;
import ir.ac.ut.ece.ie.miobook.model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MainTest {

    private UserDatabase userDatabase;
    private BookDatabase bookDatabase;
    private AuthorDatabase authorDatabase;
    private ReviewDatabase reviewDatabase;
    private User adminUser;
    private Author author;
    private Book book;
    private User normalUser;

    @BeforeEach
    public void setUp() {
        userDatabase = UserDatabase.getInstance();
        bookDatabase = BookDatabase.getInstance();
        authorDatabase = AuthorDatabase.getInstance();
        reviewDatabase = ReviewDatabase.getInstance();
        Address address = new Address("USA", "New York");
        adminUser = new User("admin", "admin_user", "1234", "admin@mail.com", address);
        adminUser.setRole(Role.admin);
        userDatabase.addUser(adminUser);

        author = new Author(
                "sample name",
                "Pen Name",
                "Nationality",
                MySimpleDate.of(1900, 1, 1),
                MySimpleDate.of(1980, 1, 1)
        );
        authorDatabase.addAuthor(author);

        List<String> genres = new ArrayList<>();
        genres.add("Fantasy");
        book = new Book("The Magical Adventure", author, "Publisher", 2023, genres, 199, "Synopsis", "Content");
        bookDatabase.addBook(book);

        normalUser = new User("customer", "normal_user", "1234", "normal@mail.com", address);
        userDatabase.addUser(normalUser);
    }

    @AfterEach
    public void tearDown() {
        userDatabase.getUsers().clear();
        bookDatabase.getBooks().clear();
        authorDatabase.getAuthors().clear();
        reviewDatabase.getReviews().clear();
    }

//    part1
//    add_user {"role": "customer", "username": "test-name", "password": "1234", "email": "my.mail@mail.com", "address": {"country": "Iran", "city": "Karaj"}}

    @Test
    public void testAddUserSuccess() throws IOException {
        String input = "add_user {\"role\": \"customer\", \"username\": \"test-name\", \"password\": \"1234\", \"email\": \"my.mail@mail.com\", \"address\": {\"country\": \"Iran\", \"city\": \"Karaj\"}}";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        Main.main(new String[]{});

        String expectedOutput = "{\"success\": true, \"message\": \"User added successfully.\"}\n";
        assertEquals(expectedOutput, outContent.toString());
    }

//    part2
//    add_author {"username": "admin_user", "name": "New Author Name", "nationality": "nationality", "penName": "New Pen Name", "born": "1982-04-12"}
    @Test
    public void testAddAuthorSuccess() throws IOException {
        String input = "add_author {\"username\": \"admin_user\", \"name\": \"New Author Name\", \"nationality\": \"nationality\", \"penName\": \"New Pen Name\", \"born\": \"1982-04-12\"}";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        Main.main(new String[]{});

        String expectedOutput = "{\"success\": true, \"message\": \"Author added successfully.\"}\n";
        assertEquals(expectedOutput, outContent.toString());
        assertEquals(2, authorDatabase.getAuthors().size());
    }

//    part3
//    add_book {"username": "admin_user", "title": "sample book", "author": "sample name", "publisher": "name", "year": 2012, "price": 250, "synopsis": "lorem", "content": "lorem ipsum", "genres": ["horror", "thriller"]}
    @Test
    public void testAddBookSuccess() throws IOException {
        String input = "add_book {\"username\": \"admin_user\", \"title\": \"sample book\", \"author\": \"sample name\", \"publisher\": \"name\", \"year\": 2012, \"price\": 250, \"synopsis\": \"lorem\", \"content\": \"lorem ipsum\", \"genres\": [\"horror\", \"thriller\"]}";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        Main.main(new String[]{});

        String expectedOutput = "{\"success\": true, \"message\": \"Book added successfully.\"}\n";
        assertEquals(expectedOutput, outContent.toString());
        assertEquals(2, bookDatabase.getBooks().size()); // Check if the book was added
    }

    @Test
    public void testAddAuthorMissingField() throws IOException {
        String input = "add_author {\"username\": \"admin_user\", \"name\": \"New Author Name\", \"born\": \"1982-04-12\"}"; // missing penName
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        Main.main(new String[]{});

        String expectedOutput = "{\"success\": false, \"message\": \"Missing required fields.\"}\n";
        assertEquals(expectedOutput, outContent.toString());
        assertEquals(1, authorDatabase.getAuthors().size());
    }

    @Test
    public void testAddAuthorInvalidDate() throws IOException {
        String input = "add_author {\"username\": \"admin_user\", \"name\": \"New Author Name\", \"nationality\": \"nationality\", \"penName\": \"New Pen Name\", \"born\": \"invalid-date\"}";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        Main.main(new String[]{});

        String expectedOutput = "{\"success\": false, \"message\": \"Invalid date format. Please use yyyy-MM-dd.\"}\n";
        assertEquals(expectedOutput, outContent.toString());
        assertEquals(1, authorDatabase.getAuthors().size());
    }

//    part6
//    add_credit {"username": "test-name", "credit": 8200}
    @Test
    public void testAddCreditSuccess() throws IOException {
        String addUserInput = "add_user {\"role\": \"customer\", \"username\": \"test-name\", \"password\": \"1234\", \"email\": \"my.mail@mail.com\", \"address\": {\"country\": \"Iran\", \"city\": \"Karaj\"}}";
        InputStream addUserIn = new ByteArrayInputStream(addUserInput.getBytes());
        System.setIn(addUserIn);

        ByteArrayOutputStream addUserOutContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(addUserOutContent));

        Main.main(new String[]{});

        String addCreditInput = "add_credit {\"username\": \"test-name\", \"credit\": 8200}";
        InputStream addCreditIn = new ByteArrayInputStream(addCreditInput.getBytes());
        System.setIn(addCreditIn);

        ByteArrayOutputStream addCreditOutContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(addCreditOutContent));

        Main.main(new String[]{});

        String expectedOutput = "{\"success\": true, \"message\": \"Credit added successfully.\"}\n";
        assertEquals(expectedOutput, addCreditOutContent.toString());
    }

//    part10
//    show_user_details {"username": "test-name"}
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

//    part11
//    show_author_details {"username": "sample name"}
    @Test
    public void testShowAuthorDetailsSuccess() throws IOException {
        String input = "show_author_details {\"username\": \"sample name\"}";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        Main.main(new String[]{});

        String output = outContent.toString();

        assertTrue(output.contains("\"success\":true"));
        assertTrue(output.contains("\"message\":\"Author details retrieved successfully.\""));
        assertTrue(output.contains("\"name\":\"sample name\""));
        assertTrue(output.contains("\"penName\":\"Pen Name\""));
        assertTrue(output.contains("\"nationality\":\"Nationality\""));
        assertTrue(output.contains("\"born\":\"1900-01-01\""));
        assertTrue(output.contains("\"died\":\"1980-01-01\""));
    }

//    part12
//    show_book_details {"title": "The Magical Adventure"}
    @Test
    public void testShowBookDetailsSuccess() throws IOException {
        String input = "show_book_details {\"title\": \"The Magical Adventure\"}";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        Main.main(new String[]{});

        String output = outContent.toString();

        assertTrue(output.contains("\"success\":true"));
        assertTrue(output.contains("\"message\":\"Book details retrieved successfully.\""));
        assertTrue(output.contains("\"title\":\"The Magical Adventure\""));
        assertTrue(output.contains("\"author\":{\"name\":\"sample name\""));
        assertTrue(output.contains("\"publisher\":\"Publisher\""));
        assertTrue(output.contains("\"genres\":[\"Fantasy\"]"));
        assertTrue(output.contains("\"year\":2023"));
        assertTrue(output.contains("\"price\":199"));
        assertTrue(output.contains("\"synopsis\":\"Synopsis\""));
    }

//    @Test
//    public void testAddCartSuccess() throws IOException {
//        String input = "add_cart {\"username\": \"normal_user\", \"title\": \"The Magical Adventure\"}";
////        add_cart {"username": "normal_user", "title": "The Magical Adventure"}
//        InputStream in = new ByteArrayInputStream(input.getBytes());
//        System.setIn(in);
//
//        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
//        System.setOut(new PrintStream(outContent));
//
//        Main.main(new String[]{});
//
//        String expectedOutput = "{\"success\": true, \"message\": \"Added book to cart.\"}\n";
//        assertEquals(expectedOutput, outContent.toString());
//        assertEquals(1, normalUser.getCart().size());
//    }

//    @Test
//    public void testRemoveCartSuccess() throws IOException {
//        normalUser.getCart().add(new CartElement(book, 0));
//        String input = "remove_cart {\"username\": \"normal_user\", \"title\": \"The Magical Adventure\"}";
////        remove_cart {"username": "normal_user", "title": "The Magical Adventure"}
//        InputStream in = new ByteArrayInputStream(input.getBytes());
//        System.setIn(in);
//
//        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
//        System.setOut(new PrintStream(outContent));
//
//        Main.main(new String[]{});
//
//        String expectedOutput = "{\"success\": true, \"message\": \"Removed book from cart.\"}\n";
//        assertEquals(expectedOutput, outContent.toString());
//        assertTrue(normalUser.getCart().isEmpty());
//    }

//    part14
//    show_book_reviews {"title": "The Magical Adventure"}
    @Test
    public void testShowBookReviewsSuccess() throws IOException {
        // Add a review first
        Review review1 = new Review("normal_user", "The Magical Adventure", 4, "Nice.");
        Review review2 = new Review("admin_user", "The Magical Adventure", 5, "Very nice.");
        reviewDatabase.addReview(review1);
        reviewDatabase.addReview(review2);

        String input = "show_book_reviews {\"title\": \"The Magical Adventure\"}";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        Main.main(new String[]{});

        String output = outContent.toString();

        assertTrue(output.contains("\"success\":true"));
        assertTrue(output.contains("\"message\":\"Book reviews retrieved successfully.\""));
        assertTrue(output.contains("\"title\":\"The Magical Adventure\""));

        assertTrue(output.contains("\"reviews\":["));

        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualReviews = mapper.readTree(output)
                .get("data")
                .get("reviews");

        JsonNode expectedReview1 = mapper.readTree("{\"username\":\"normal_user\",\"rate\":4,\"comment\":\"Nice.\"}");
        JsonNode expectedReview2 = mapper.readTree("{\"username\":\"admin_user\",\"rate\":5,\"comment\":\"Very nice.\"}");

        boolean foundReview1 = false;
        boolean foundReview2 = false;

        for (JsonNode actualReview : actualReviews) {
            if (actualReview.equals(expectedReview1)) {
                foundReview1 = true;
            }
            if (actualReview.equals(expectedReview2)) {
                foundReview2 = true;
            }
        }

        assertTrue(foundReview1);
        assertTrue(foundReview2);

        assertTrue(output.contains("\"averageRating\":4"));
    }

//    part18.1
//    search_books_by_title {"title": "The Magical Adventure"}
    @Test
    public void testSearchBooksByTitleSuccess() throws IOException {
        String input = "search_books_by_title {\"title\": \"The Magical Adventure\"}";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        Main.main(new String[]{});

        String output = outContent.toString();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualOutput = mapper.readTree(output);

        Assertions.assertTrue(actualOutput.get("success").asBoolean());
        Assertions.assertEquals("Books containing 'The Magical Adventure' in their title:", actualOutput.get("message").asText());
        Assertions.assertEquals("The Magical Adventure", actualOutput.get("data").get("search").asText());

        JsonNode expectedBook = mapper.readTree("{\"title\":\"The Magical Adventure\"}");
        JsonNode actualBooks = actualOutput.get("data").get("books");

        boolean bookFound = false;
        for (JsonNode actualBook : actualBooks) {
            if (actualBook.get("title").asText().equals("The Magical Adventure")) {
                bookFound = true;
                break;
            }
        }
        Assertions.assertTrue(bookFound);
    }

//    part18.2
//    search_books_by_author {"name": "sample name"}
    @Test
    public void testSearchBooksByAuthorSuccess() throws IOException {
        String input = "search_books_by_author {\"name\": \"sample name\"}";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        Main.main(new String[]{});

        String output = outContent.toString();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualOutput = mapper.readTree(output);

        Assertions.assertTrue(actualOutput.get("success").asBoolean());
        Assertions.assertEquals("Books by 'sample name':", actualOutput.get("message").asText());
        Assertions.assertEquals("sample name", actualOutput.get("data").get("search").asText());

        JsonNode actualBooks = actualOutput.get("data").get("books");

        boolean bookFound = false;
        for (JsonNode actualBook : actualBooks) {
            if (actualBook.get("author").asText().equals("sample name")) {
                bookFound = true;
                break;
            }
        }
        Assertions.assertTrue(bookFound);
    }

//    part18.3
//    search_books_by_genre {"genre": "Fantasy"}
    @Test
    public void testSearchBooksByGenreSuccess() throws IOException {
        String input = "search_books_by_genre {\"genre\": \"Fantasy\"}";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        Main.main(new String[]{});

        String output = outContent.toString();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualOutput = mapper.readTree(output);

        Assertions.assertTrue(actualOutput.get("success").asBoolean());
        Assertions.assertEquals("Books in the 'Fantasy' genre:", actualOutput.get("message").asText());
        Assertions.assertEquals("Fantasy", actualOutput.get("data").get("search").asText());

        JsonNode actualBooks = actualOutput.get("data").get("books");

        boolean bookFound = false;
        for (JsonNode actualBook : actualBooks) {
            if (actualBook.get("title").asText().equals("The Magical Adventure")) {
                bookFound = true;
                break;
            }
        }
        Assertions.assertTrue(bookFound);
    }

//    part18.4
//    search_books_by_year {"from": 2023, "to": 2023}
    @Test
    public void testSearchBooksByYearSuccess() throws IOException {
        String input = "search_books_by_year {\"from\": 2023, \"to\": 2023}";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        Main.main(new String[]{});

        String output = outContent.toString();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualOutput = mapper.readTree(output);

        Assertions.assertTrue(actualOutput.get("success").asBoolean());
        Assertions.assertEquals("Books published from 2023 to 2023:", actualOutput.get("message").asText());
        Assertions.assertEquals("2023-2023", actualOutput.get("data").get("search").asText());

        JsonNode actualBooks = actualOutput.get("data").get("books");

        boolean bookFound = false;
        for (JsonNode actualBook : actualBooks) {
            if (actualBook.get("title").asText().equals("The Magical Adventure")) {
                bookFound = true;
                break;
            }
        }
        Assertions.assertTrue(bookFound);
}

package ir.ac.ut.ece.ie.miobook.data;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.ac.ut.ece.ie.miobook.database.AuthorDatabase;
import ir.ac.ut.ece.ie.miobook.database.BookDatabase;
import ir.ac.ut.ece.ie.miobook.database.ReviewDatabase;
import ir.ac.ut.ece.ie.miobook.database.UserDatabase;
import ir.ac.ut.ece.ie.miobook.model.Author;
import ir.ac.ut.ece.ie.miobook.model.Book;
import ir.ac.ut.ece.ie.miobook.model.Review;
import ir.ac.ut.ece.ie.miobook.model.User;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

@Component
public class DataInitializer implements ApplicationRunner {

    private final ObjectMapper mapper = new ObjectMapper();

    private final UserDatabase userDatabase;
    private final AuthorDatabase authorDatabase;
    private final BookDatabase bookDatabase;
    private final ReviewDatabase reviewDatabase;

    public DataInitializer(UserDatabase userDatabase, AuthorDatabase authorDatabase,
                           BookDatabase bookDatabase, ReviewDatabase reviewDatabase) {
        this.userDatabase = userDatabase;
        this.authorDatabase = authorDatabase;
        this.bookDatabase = bookDatabase;
        this.reviewDatabase = reviewDatabase;
    }

    public void initializeData() {
        initializeUsers();
        initializeAuthors();
        initializeBooks();
        initializeReviews();
    }

    private void initializeUsers() {
        try {
            URL url = new URL("http://194.60.230.196:8000/users");
            List<User> users = fetchData(url, new TypeReference<List<User>>() {});
            if(users != null) {
                for (User user : users) {
                    userDatabase.addUser(user);
                }
            }
        } catch (IOException e) {
            System.err.println("Error initializing users: " + e.getMessage());
        }
    }

    private void initializeAuthors() {
        try {
            URL url = new URL("http://194.60.230.196:8000/authors");
            List<Author> authors = fetchData(url, new TypeReference<List<Author>>() {});
            if(authors != null) {
                for (Author author : authors) {
                    authorDatabase.addAuthor(author);
                }
            }
        } catch (IOException e) {
            System.err.println("Error initializing authors: " + e.getMessage());
        }
    }

    private void initializeBooks() {
        try {
            URL url = new URL("http://194.60.230.196:8000/books");
            List<Book> books = fetchData(url, new TypeReference<List<Book>>() {});
            if(books != null) {
                for (Book book : books) {
                    bookDatabase.addBook(book);
                }
            }
        } catch (IOException e) {
            System.err.println("Error initializing books: " + e.getMessage());
        }
    }

    private void initializeReviews() {
        try {
            URL url = new URL("http://194.60.230.196:8000/reviews");
            List<Review> reviews = fetchData(url, new TypeReference<List<Review>>() {});
            if(reviews != null) {
                for (Review review : reviews) {
                    reviewDatabase.addReview(review);
                }
            }
        } catch (IOException e) {
            System.err.println("Error initializing reviews: " + e.getMessage());
        }
    }

    private <T> List<T> fetchData(URL url, TypeReference<List<T>> typeReference) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            connection.disconnect();
            return mapper.readValue(response.toString(), typeReference);

        } else {
            System.err.println("Failed to fetch data from: " + url + ". Response code: " + responseCode);
            connection.disconnect();
            return null;
        }
    }

    @Override
    public void run(ApplicationArguments args) {
        initializeData();
    }
}
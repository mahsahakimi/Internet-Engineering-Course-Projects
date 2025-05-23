package ir.ac.ut.ece.ie.miobook.data;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.ac.ut.ece.ie.miobook.repository.AuthorRepository;
import ir.ac.ut.ece.ie.miobook.database.BookDatabase;
import ir.ac.ut.ece.ie.miobook.database.ReviewDatabase;
import ir.ac.ut.ece.ie.miobook.model.*;
import ir.ac.ut.ece.ie.miobook.repository.UserRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class DataInitializer implements ApplicationRunner {

    private final ObjectMapper mapper = new ObjectMapper();

    private final UserRepository userRepository;
    private final AuthorRepository authorRepository;
    private final BookDatabase bookDatabase;
    private final ReviewDatabase reviewDatabase;

    public DataInitializer(UserRepository userRepository, AuthorRepository authorRepository,
                           BookDatabase bookDatabase, ReviewDatabase reviewDatabase) {
        this.userRepository = userRepository;
        this.authorRepository = authorRepository;
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
            URL url = new URL("http://194.60.231.242:8000/users");
            List<Map<String, Object>> userMaps = fetchData(url, new TypeReference<List<Map<String, Object>>>() {});
            if (userMaps != null) {
                for (Map<String, Object> userMap : userMaps) {
                    String roleStr = (String) userMap.get("role");
                    String username = (String) userMap.get("username");
                    String password = (String) userMap.get("password");
                    String email = (String) userMap.get("email");
                    Map<String, Object> addressMap = (Map<String, Object>) userMap.get("address");

                    if (addressMap != null) {
                        Address address = new Address(
                                (String) addressMap.get("city"),
                                (String) addressMap.get("country")
                        );

                        User user;
                        try {
                            Role role = Role.valueOf(roleStr.toUpperCase());
                            if (role == Role.ADMIN) {
                                user = new Admin(username, password, email, address);
                            } else if (role == Role.CUSTOMER) {
                                user = new Customer(username, password, email, address);
                            } else {
                                System.err.println("Unknown role: " + roleStr + " for user: " + username);
                                continue;
                            }
                            userRepository.save(user);
                        } catch (IllegalArgumentException e) {
                            System.err.println("Invalid role: " + roleStr + " for user: " + username);
                        }
                    } else {
                        System.err.println("Address is null for user: " + username);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error initializing users: " + e.getMessage());
        }
    }

    private void initializeAuthors() {
        try {
            URL url = new URL("http://194.60.231.242:8000/authors");
            List<Map<String, Object>> authorMaps = fetchData(url, new TypeReference<List<Map<String, Object>>>() {});
            if (authorMaps != null) {
                for (Map<String, Object> authorMap : authorMaps) {
                    String name = (String) authorMap.get("name");
                    String penName = (String) authorMap.get("penName");
                    String nationality = (String) authorMap.get("nationality");
                    String bornStr = (String) authorMap.get("born");
                    String diedStr = (String) authorMap.get("died");
                    String addedByAdminUsername = (String) authorMap.get("addedBy"); // Assuming API provides the adding admin's username

                    MyLocalDate born = parseLocalDate((String) authorMap.get("born"));
                    MyLocalDate died = parseLocalDate((String) authorMap.get("died"));

                    Optional<Admin> adminOptional = adminRepository.findById(addedByAdminUsername);

                    if (adminOptional.isPresent()) {
                        Admin addingAdmin = adminOptional.get();
                        Optional<Author> existingAuthor = authorRepository.findById(name);

                        if (existingAuthor.isEmpty()) {
                            Author author = new Author(name, penName, nationality, born, died, addingAdmin);
                            authorRepository.save(author);
                            System.out.println("Author added: " + name + " (added by " + addedByAdminUsername + ")");
                        } else {
                            System.out.println("Author already exists: " + name);
                            // You could add logic here to update the author if needed
                        }
                    } else {
                        System.err.println("Admin not found: " + addedByAdminUsername + " for author: " + name);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error initializing authors: " + e.getMessage());
        }
    }

    // Helper method to parse date strings to MyLocalDate (assuming yyyy-mm-dd format)
    private MyLocalDate parseLocalDate(String dateStr) {
        if (dateStr != null && !dateStr.isEmpty()) {
            String[] parts = dateStr.split("-");
            if (parts.length == 3) {
                try {
                    int year = Integer.parseInt(parts[0]);
                    int month = Integer.parseInt(parts[1]);
                    int day = Integer.parseInt(parts[2]);
                    return new MyLocalDate(year, month, day);
                } catch (NumberFormatException e) {
                    System.err.println("Error parsing date: " + dateStr);
                    return null;
                }
            } else {
                System.err.println("Invalid date format: " + dateStr);
                return null;
            }
        }
        return null;
    }

    private void initializeBooks() {
        try {
            URL url = new URL("http://194.60.231.242:8000/books");
            List<Map<String, Object>> bookMaps = fetchData(url, new TypeReference<List<Map<String, Object>>>() {});

            if (bookMaps != null) {
                for (Map<String, Object> bookMap : bookMaps) {
                    String authorName = (String) bookMap.get("author");

                    Author foundAuthor = authorDatabase.findAuthorByName(authorName);

                    if (foundAuthor != null) {
                        Book fixedBook = new Book(
                                (String) bookMap.get("title"),
                                foundAuthor,
                                (String) bookMap.get("publisher"),
                                (Integer) bookMap.get("year"),
                                (List<String>) bookMap.get("genres"),
                                (Integer) bookMap.get("price"),
                                (String) bookMap.get("synopsis"),
                                (String) bookMap.get("content")
                        );

                        bookDatabase.addBook(fixedBook);
                    } else {
                        System.err.println("Author not found: " + authorName + " for book: " + bookMap.get("title"));
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error initializing books: " + e.getMessage());
        }
    }

    private void initializeReviews() {
        try {
            URL url = new URL("http://194.60.231.242:8000/reviews");
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
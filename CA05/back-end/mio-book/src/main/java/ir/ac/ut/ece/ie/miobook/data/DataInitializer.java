package ir.ac.ut.ece.ie.miobook.data;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.ac.ut.ece.ie.miobook.repository.AuthorRepository;
import ir.ac.ut.ece.ie.miobook.repository.GenreRepository;
import ir.ac.ut.ece.ie.miobook.repository.BookRepository;
import ir.ac.ut.ece.ie.miobook.model.*;
import ir.ac.ut.ece.ie.miobook.repository.ReviewRepository;
import ir.ac.ut.ece.ie.miobook.repository.UserRepository;
import ir.ac.ut.ece.ie.miobook.repository.WalletRepository;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Optional;

@Component
public class DataInitializer implements ApplicationRunner {

    private final ObjectMapper mapper = new ObjectMapper();

    private final UserRepository userRepository;
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final GenreRepository genreRepository;
    private final ReviewRepository reviewRepository;
    private final WalletRepository walletRepository;

    public DataInitializer(UserRepository userRepository, AuthorRepository authorRepository,
                           BookRepository bookRepository, GenreRepository genreRepository,
                            ReviewRepository  reviewRepository, WalletRepository walletRepository) {
        this.userRepository = userRepository;
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
        this.genreRepository = genreRepository;
        this.reviewRepository = reviewRepository;
        this.walletRepository = walletRepository;
    }

    public void initializeData() {
        initializeUsers();
        initializeAuthors();
        initializeBooks();
        initializeReviews();
    }

    private void initializeUsers() {
        try {
            URL url = URI.create("http://194.60.231.242:8000/users").toURL();
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
                        
                        Optional<User> existing = userRepository.findById("user1");
                        if (existing.isEmpty()) {
                            User user;
                            try {
                                Role role = Role.valueOf(roleStr.toUpperCase());
                                if (role == Role.ADMIN) {
                                    user = new Admin(username, password, email, address);
                                } else if (role == Role.CUSTOMER) {
                                    Customer customer = new Customer(username, password, email, address);
                                    Wallet w = new Wallet(customer);
                                    customer.setWallet(w);
                                    user = customer;
                                } else {
                                    System.err.println("Unknown role: " + roleStr + " for user: " + username);
                                    continue;
                                }
                                userRepository.save(user);
                                System.out.println("User added: " + username);
                            } catch (IllegalArgumentException e) {
                                System.err.println("Invalid role: " + roleStr + " for user: " + username);
                            }
                        } else{
                            System.out.println("User already exists: " + username);
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
            URL url = URI.create("http://194.60.231.242:8000/authors").toURL();
            List<Map<String, Object>> authorMaps = fetchData(url, new TypeReference<List<Map<String, Object>>>() {});
            if (authorMaps != null) {
                for (Map<String, Object> authorMap : authorMaps) {
                    String name = (String) authorMap.get("name");
                    String penName = (String) authorMap.get("penName");
                    String nationality = (String) authorMap.get("nationality");
                    String bornStr = (String) authorMap.get("born");
                    String diedStr = (String) authorMap.get("died");
                    String adminUsername = (String) authorMap.get("username");

                    MyLocalDate born = MyLocalDate.parse(bornStr);
                    MyLocalDate died = (diedStr != null && !diedStr.isEmpty())
                            ? MyLocalDate.parse(diedStr)
                            : null;

                    Optional<Admin> adminOptional = userRepository.findAdminByUsername(adminUsername);

                    if (adminOptional.isPresent()) {
                        Admin addingAdmin = adminOptional.get();
                        Optional<Author> existingAuthor = authorRepository.findById(name);

                        if (existingAuthor.isEmpty()) {
                            Author author = new Author(name, penName, nationality, born, died, addingAdmin);
                            authorRepository.save(author);
                            System.out.println("Author added: " + name + " (added by " + adminUsername + ")");
                        } else {
                            System.out.println("Author already exists: " + name);
                        }
                    } else {
                        System.err.println("Admin not found: " + adminUsername + " for author: " + name);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error initializing authors: " + e.getMessage());
        }
    }

    private void initializeBooks() {
        try {
            URL url = URI.create("http://194.60.231.242:8000/books").toURL();
            List<Map<String, Object>> bookMaps = fetchData(url, new TypeReference<>() {});

            if (bookMaps != null) {
                for (Map<String, Object> bookMap : bookMaps) {
                    String authorName = (String) bookMap.get("author");
                    Optional<Author> optionalAuthor = authorRepository.findByName(authorName);
                    
                    if (optionalAuthor.isEmpty()) {
                        System.err.println("Author not found: " + authorName);
                        continue;
                    }

                    Author author = optionalAuthor.get();
                    String title = (String) bookMap.get("title");

                    if (bookRepository.existsByTitleAndAuthor(title, author)) {
                        System.out.println("Book already exists: " + title + " by " + authorName);
                        continue;
                    }

                    Book book = new Book();
                    book.setTitle(title);
                    book.setAuthor(author);
                    book.setPublisher((String) bookMap.get("publisher"));
                    book.setYear((Integer) bookMap.get("year"));
                    book.setPrice((Integer) bookMap.get("price"));
                    book.setSynopsis((String) bookMap.get("synopsis"));
                    book.setContent((String) bookMap.get("content"));

                    List<String> genreNames = (List<String>) bookMap.get("genres");
                    Set<Genre> genreEntities = new HashSet<>();

                    for (String genreName : genreNames) {
                        String trimmedName = genreName.trim();

                        Genre genre = genreRepository
                            .findByNameIgnoreCase(trimmedName)
                            .orElseGet(() -> {
                                Genre newGenre = new Genre();
                                newGenre.setName(trimmedName);
                                return genreRepository.save(newGenre);
                            });

                        genreEntities.add(genre);
                    }

                    book.setGenres(genreEntities);
                    bookRepository.save(book);
                    System.out.println("Book added: " + title + " by " + authorName);
                }
            }

        } catch (IOException e) {
            System.err.println("Error initializing books: " + e.getMessage());
        }
    }



    private void initializeReviews() {
        try {
            URL url = URI.create("http://194.60.231.242:8000/reviews").toURL();
            List<Map<String, Object>> reviewMaps = fetchData(url, new TypeReference<List<Map<String, Object>>>() {});

            if (reviewMaps != null) {
                for (Map<String, Object> reviewMap : reviewMaps) {
                    try {
                        String username = (String) reviewMap.get("username");
                        String bookTitle = (String) reviewMap.get("title");
                        Integer rate = (Integer) reviewMap.get("rate");
                        String comment = (String) reviewMap.get("comment");
                        String dateStr = (String) reviewMap.get("date");

                        // Find customer and book entities
                        Optional<Customer> customerOptional = userRepository.findCustomerByUsername(username);
                        Book book = bookRepository.findByTitle(bookTitle).orElse(null);

                        if (customerOptional.isPresent() && book != null) {
                            Customer customer = customerOptional.get();
                            MyLocalDateTime createdAt = MyLocalDateTime.parse(dateStr);

                            // Check if review already exists
                            boolean reviewExists = reviewRepository.existsByCustomerAndBook(customer, book);

                            if (!reviewExists) {
                                Review review = new Review(
                                        customer,
                                        book,
                                        rate,
                                        comment,
                                        createdAt
                                );
                                reviewRepository.save(review);
                                System.out.println("Review added for book: " + bookTitle + " by user: " + username);
                            } else {
                                System.out.println("Review already exists for book: " + bookTitle + " by user: " + username);
                            }
                        } else {
                            String errorMsg = "Could not create review - ";
                            errorMsg += customerOptional.isEmpty() ? "Customer not found: " + username + " " : "";
                            errorMsg += book == null ? "Book not found: " + bookTitle : "";
                            System.err.println(errorMsg);
                        }
                    } catch (Exception e) {
                        System.err.println("Error processing review: " + reviewMap + " - " + e.getMessage());
                    }
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
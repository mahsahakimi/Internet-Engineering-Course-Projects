package ir.ac.ut.ece.ie.miobook.data;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.ac.ut.ece.ie.miobook.configuration.DefaultAdminsProperties;
import ir.ac.ut.ece.ie.miobook.model.*;
import ir.ac.ut.ece.ie.miobook.repository.AuthorRepository;
import ir.ac.ut.ece.ie.miobook.repository.BookRepository;
import ir.ac.ut.ece.ie.miobook.repository.GenreRepository;
import ir.ac.ut.ece.ie.miobook.repository.ReviewRepository;
import ir.ac.ut.ece.ie.miobook.repository.UserRepository;
import ir.ac.ut.ece.ie.miobook.utils.PasswordHasher;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Component
public class DataInitializer implements ApplicationRunner {

    private static final String SEED_DATA_PATH = "seed-data.json";

    private final ObjectMapper mapper = new ObjectMapper();

    private final UserRepository userRepository;
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final GenreRepository genreRepository;
    private final ReviewRepository reviewRepository;
    private final DefaultAdminsProperties defaultAdminsProperties;

    public DataInitializer(UserRepository userRepository, AuthorRepository authorRepository,
                           BookRepository bookRepository, GenreRepository genreRepository,
                           ReviewRepository reviewRepository,
                           DefaultAdminsProperties defaultAdminsProperties) {
        this.userRepository = userRepository;
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
        this.genreRepository = genreRepository;
        this.reviewRepository = reviewRepository;
        this.defaultAdminsProperties = defaultAdminsProperties;
    }

    public void initializeData() {
        initializeDefaultAdmins();
        System.out.println("\n --- Start loading seed data from : " + SEED_DATA_PATH + " ---\n");
        try (InputStream inputStream = new ClassPathResource(SEED_DATA_PATH).getInputStream()) {
            SeedData seedData = mapper.readValue(inputStream, new TypeReference<>() {});
            initializeUsers(seedData.users());
            initializeAuthors(seedData.authors());
            initializeBooks(seedData.books());
            initializeReviews(seedData.reviews());
        } catch (IOException e) {
            System.err.println("Error reading seed data file: " + e.getMessage());
        }
        System.out.println("\n --- Server is on ---\n");
    }

    private void initializeDefaultAdmins() {
        for (DefaultAdminsProperties.AdminConfig adminConfig : defaultAdminsProperties.getDefaultAdmins()) {
            createAdminIfNotExists(
                    adminConfig.getUsername(),
                    adminConfig.getPassword(),
                    adminConfig.getEmail()
            );
        }
    }

    private void createAdminIfNotExists(String username, String rawPassword, String email) {
        if (username == null || username.isBlank()
                || rawPassword == null || rawPassword.isBlank()
                || email == null || email.isBlank()) {
            return;
        }
        if (userRepository.existsByUsername(username) || userRepository.existsByEmail(email)) {
            return;
        }

        String salt = PasswordHasher.generateSalt();
        String hashedPassword = PasswordHasher.hashPassword(rawPassword, salt);
        Address address = new Address("Iran", "Tehran");
        Admin admin = new Admin(username, hashedPassword, salt, email, address);
        userRepository.save(admin);
    }

    @SuppressWarnings("unchecked")
    private void initializeUsers(List<Map<String, Object>> userMaps) {
        if (userMaps == null) {
            return;
        }
        for (Map<String, Object> userMap : userMaps) {
            String roleStr = (String) userMap.get("role");
            String username = (String) userMap.get("username");
            String password = (String) userMap.get("password");
            String email = (String) userMap.get("email");
            Map<String, Object> addressMap = (Map<String, Object>) userMap.get("address");

            if (addressMap == null) {
                System.err.println("Address is null for user: " + username);
                continue;
            }

            Address address = new Address(
                    (String) addressMap.get("country"),
                    (String) addressMap.get("city")
            );

            Optional<User> existing = userRepository.findByUsername(username);
            if (existing.isEmpty()) {
                try {
                    Role role = Role.valueOf(roleStr.toUpperCase());
                    String salt = PasswordHasher.generateSalt();
                    String hashedPassword = PasswordHasher.hashPassword(password, salt);

                    User user;
                    if (role == Role.ADMIN) {
                        user = new Admin(username, hashedPassword, salt, email, address);
                    } else if (role == Role.CUSTOMER) {
                        Customer customer = new Customer(username, hashedPassword, salt, email, address);
                        Wallet w = new Wallet(customer);
                        customer.setWallet(w);
                        user = customer;
                    } else {
                        System.err.println("Unknown role: " + roleStr + " for user: " + username);
                        continue;
                    }
                    userRepository.save(user);
                } catch (IllegalArgumentException e) {
                    System.err.println("Invalid role: " + roleStr + " for user: " + username);
                }
            }
        }
        System.out.println("=> Users added successfully");
    }

    private void initializeAuthors(List<Map<String, Object>> authorMaps) {
        if (authorMaps == null) {
            return;
        }
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
                }
            } else {
                System.err.println("Admin not found: " + adminUsername + " for author: " + name);
            }
        }
        System.out.println("=> Authors added successfully");
    }

    @SuppressWarnings("unchecked")
    private void initializeBooks(List<Map<String, Object>> bookMaps) {
        if (bookMaps == null) {
            return;
        }
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

            if (genreNames != null) {
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
            }

            book.setGenres(genreEntities);
            bookRepository.save(book);
        }
        System.out.println("=> Books added successfully");
    }

    private void initializeReviews(List<Map<String, Object>> reviewMaps) {
        if (reviewMaps == null) {
            return;
        }
        for (Map<String, Object> reviewMap : reviewMaps) {
            try {
                String username = (String) reviewMap.get("username");
                String bookTitle = (String) reviewMap.get("title");
                Integer rate = (Integer) reviewMap.get("rate");
                String comment = (String) reviewMap.get("comment");
                String dateStr = (String) reviewMap.get("date");

                Optional<Customer> customerOptional = userRepository.findCustomerByUsername(username);
                Book book = bookRepository.findByTitle(bookTitle).orElse(null);

                if (customerOptional.isPresent() && book != null) {
                    Customer customer = customerOptional.get();
                    MyLocalDateTime createdAt = MyLocalDateTime.parse(dateStr);

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
        System.out.println("=> Reviews added successfully");
    }

    @Override
    public void run(ApplicationArguments args) {
        initializeData();
    }

    private record SeedData(
            List<Map<String, Object>> users,
            List<Map<String, Object>> authors,
            List<Map<String, Object>> books,
            List<Map<String, Object>> reviews
    ) {}
}

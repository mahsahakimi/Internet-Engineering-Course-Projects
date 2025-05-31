package ir.ac.ut.ece.ie.miobook.service;

import ir.ac.ut.ece.ie.miobook.exceptions.*;
import ir.ac.ut.ece.ie.miobook.model.*;
import ir.ac.ut.ece.ie.miobook.repository.*;
import ir.ac.ut.ece.ie.miobook.context.UserContext;
import ir.ac.ut.ece.ie.miobook.utils.PaginationUtils;
import jakarta.transaction.Transactional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class BookService {

    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final UserBookRepository userBookRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;
    private final ReviewRepository reviewRepository;
    private final UserContext userContext;

    public BookService(UserRepository userRepository, BookRepository bookRepository,
                        UserBookRepository userBookRepository,
                        AuthorRepository authorRepository, GenreRepository genreRepository,
                        ReviewRepository reviewRepository, UserContext userContext) {
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
        this.userBookRepository = userBookRepository;
        this.authorRepository = authorRepository;
        this.genreRepository = genreRepository;
        this.reviewRepository = reviewRepository;
        this.userContext = userContext;
    }

    public void addBook(String title, String authorName, String publisher, int year, int price,
                    String genres, String synopsis, String content) {
        User user = userContext.getLoggedInUser();
        if (user == null) {
            throw new UserNotLoggedInException();
        }
        if (user.getRole() != Role.ADMIN) {
            throw new PermissionDeniedException("Only admins can add books.");
        }

        if (bookRepository.existsByTitle(title)) {
            throw new BookTitleAlreadyExistsException(title);
        }

        Author author = authorRepository.findByName(authorName)
            .orElseThrow(() -> new AuthorNotFoundException(authorName));

        if (price < 0) {
            throw new InvalidPriceValueException();
        }
        if (year < 0) {
            throw new InvalidYearException();
        }

        Book book = new Book(title, author, publisher, year, price, synopsis, content);

        List<String> genreNames = Stream.of(genres.split(","))
            .map(String::trim)
            .filter(name -> !name.isEmpty())
            .toList();
        for (String genreName : genreNames) {
            Genre genre = genreRepository.findByNameIgnoreCase(genreName)
                .orElseGet(() -> genreRepository.save(new Genre(genreName)));
            book.addGenre(genre);
        }

        bookRepository.save(book);

    }

    @Transactional
    public void borrowBook(String title, int days) {
        User user = userContext.getLoggedInUser();
        if (user == null) {
            throw new UserNotLoggedInException();
        }

        if (user.getRole() != Role.CUSTOMER) {
            throw new PermissionDeniedException(Role.ADMIN.toString());
        }

        Customer customer = (Customer) user;

        Book book = bookRepository.findByTitle(title)
                .orElseThrow(() -> new BookNotFoundException(title));

        if (days < 1 || days > 9) {
            throw new DaysOutOfRangeException();
        }

        boolean alreadyInCart = customer.getCart().stream()
                .anyMatch(cartElement -> cartElement.getBook().equals(book));
        if (alreadyInCart) {
            throw new BookAlreadyInCartException(title);
        }

        for (UserBook userBook : customer.getLibrary()) {
            if (userBook.getBook().equals(book)) {
                if (!userBook.isBorrowed()) {
                    throw new BookAlreadyOwnedException(title);
                } else if (userBook.canAccessBook()) {
                    throw new BookAlreadyBorrowedException(title);
                }
            }
        }

        CartElement newElement = new CartElement(book, days);
        newElement.setCustomer(customer);
        customer.getCart().add(newElement);
        userRepository.save(customer);
    }

    @Transactional
    public void addReview(String title, int rate, String comment) {
        User user = userContext.getLoggedInUser();
        if (user == null) {
            throw new UserNotLoggedInException();
        }
        if (user.getRole() != Role.CUSTOMER) {
            throw new PermissionDeniedException(Role.ADMIN.toString());
        }

        Customer customer = userRepository.findCustomerByUsername(user.getUsername())
            .orElseThrow(() -> new RuntimeException("Customer not found"));
        
        Book book = bookRepository.findById(title)
                .orElseThrow(() -> new BookNotFoundException(title));

        for (UserBook userBook : customer.getLibrary()) {
            if (userBook.getBook().equals(book)) {
                if (!userBook.isBorrowed() || userBook.canAccessBook()) {
                    throw new BookNotOwnedException();
                }
            }
        }

        if (rate < 1 || rate > 5) {
            throw new InvalidRatingException();
        }

        MyLocalDateTime submissionTime = MyLocalDateTime.now();
        Review review = new Review(customer, book, rate, comment, submissionTime);
        reviewRepository.save(review);
    }

    public Map<String, Object> getBookDetails(String title) {
        Book book = bookRepository.findByTitle(title)
                .orElseThrow(() -> new BookNotFoundException(title));
        Map<String, Object> bookData = new HashMap<>();
        bookData.put("title", book.getTitle());
        bookData.put("author", book.getAuthor().getName());  
        bookData.put("publisher", book.getPublisher());
        List<String> genreNames = book.getGenres()
            .stream()
            .map(Genre::getName)
            .collect(Collectors.toList());
        bookData.put("genres", genreNames); 
        bookData.put("year", book.getYear());
        bookData.put("price", book.getPrice());
        bookData.put("synopsis", book.getSynopsis());

        Double averageRating = reviewRepository.calculateAverageRatingByBook(book);
        bookData.put("averageRating", averageRating);

        return bookData;
    }

    public Map<String, Object> showBookContent(String title) {
        User user = userContext.getLoggedInUser();
        if (user == null) {
            throw new UserNotLoggedInException();
        }
        if (user.getRole() != Role.CUSTOMER) {
            throw new PermissionDeniedException(Role.ADMIN.toString());
        }

        Book book = bookRepository.findById(title)
                .orElseThrow(() -> new BookNotFoundException(title));

        Customer customer = userRepository.findCustomerByUsername(user.getUsername())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        List<UserBook> userBooks = userBookRepository.findAllByCustomerAndBook(customer, book);
        boolean hasAccess = false;
        for (UserBook userBook : userBooks) {
            if (!userBook.isBorrowed() || userBook.canAccessBook()) {
                hasAccess = true;
                break;
            }
        }
        if (!hasAccess) {
            throw new BookAccessDeniedException();
        }

        Map<String, Object> data = new HashMap<>();
        data.put("title", book.getTitle());
        data.put("author", book.getAuthor().getName());
        data.put("content", book.getContent());

        return data;
    }

    public Map<String, Object> getBookReviews(String title, int page, int pageSize) {
        Book book = bookRepository.findById(title)
                .orElseThrow(() -> new BookNotFoundException(title));

        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<Review> reviewPage = reviewRepository.findByBook(book, pageable);

        if (reviewPage.isEmpty()) {
            throw new NoReviewsException(title);
        }

        List<Map<String, Object>> outputReviews = reviewPage.getContent().stream()
                .map(r -> {
                    Map<String, Object> reviewMap = new HashMap<>();
                    reviewMap.put("username", r.getCustomer().getUsername());
                    reviewMap.put("rate", r.getRate());
                    reviewMap.put("comment", r.getComment());
                    reviewMap.put("date", r.getDate());
                    return reviewMap;
                })
                .collect(Collectors.toList());

        Double averageRating = reviewRepository.calculateAverageRatingByBook(book);
        if (averageRating == null) averageRating = 0.0;

        Map<String, Object> data = new HashMap<>();
        data.put("length", reviewRepository.countByBook(book));
        data.put("reviews", outputReviews);
        data.put("averageRating", averageRating);

        return data;
    }

    private List<Map<String, Object>> formatBooks(List<Book> books) {
        List<Map<String, Object>> formattedBooks = new ArrayList<>();
        for (Book book : books) {
            Map<String, Object> formattedBook = new HashMap<>();
            formattedBook.put("title", book.getTitle());
            formattedBook.put("author", book.getAuthor().getName());
            formattedBook.put("publisher", book.getPublisher());
            List<String> genreNames = book.getGenres()
                .stream()
                .map(Genre::getName)
                .collect(Collectors.toList());
            formattedBook.put("genres", genreNames);
            formattedBook.put("year", book.getYear());
            formattedBook.put("price", book.getPrice());
            formattedBook.put("synopsis", book.getSynopsis());
            formattedBook.put("averageRating", reviewRepository.calculateAverageRatingByBook(book));
            formattedBooks.add(formattedBook);
        }
        return formattedBooks;
    }

    private List<Map<String, Object>> shortFormatBooks(List<Book> books) {
        List<Map<String, Object>> formattedBooks = new ArrayList<>();
        for (Book book : books) {
            Map<String, Object> formattedBook = new HashMap<>();
            formattedBook.put("title", book.getTitle());
            formattedBook.put("author", book.getAuthor().getName());
            formattedBook.put("price", book.getPrice());
            formattedBook.put("averageRating", reviewRepository.calculateAverageRatingByBook(book));
            formattedBooks.add(formattedBook);
        }
        return formattedBooks;
    }

    private List<Map<String, Object>> adminPageFormatBooks(List<Book> books) {
        List<Map<String, Object>> formattedBooks = new ArrayList<>();
        for (Book book : books) {
            Map<String, Object> formattedBook = new HashMap<>();
            formattedBook.put("title", book.getTitle());
            formattedBook.put("author", book.getAuthor().getName());
            List<String> genreNames = book.getGenres()
                .stream()
                .map(Genre::getName)
                .collect(Collectors.toList());
            formattedBook.put("genres", genreNames);
            formattedBook.put("publisher", book.getPublisher());
            formattedBook.put("year", book.getYear());
            formattedBook.put("price", book.getPrice());
            // formattedBook.put("total_buys", book.getTotalBuys()); // Uncomment if you implement total buys
            formattedBooks.add(formattedBook);
        }
        return formattedBooks;
    }


    public List<Map<String, Object>> searchByTitle(String title, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<Book> bookPage = bookRepository.findByTitleContainingIgnoreCase(title, pageable);

        if (bookPage.isEmpty()) {
            throw new NoBooksFoundException();
        }

        List<Book> paginatedBooks = bookPage.getContent();
        List<Map<String, Object>> formattedBooks = formatBooks(paginatedBooks);

        return formattedBooks;
    }


    public List<Map<String, Object>> searchByAuthor(String authorName, int page, int pageSize) {
        Author author = authorRepository.findByName(authorName)
                .orElseThrow(() -> new AuthorNotFoundException(authorName));

        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<Book> booksPage = bookRepository.findByAuthor(author, pageable);
        if (booksPage.isEmpty()) {
            throw new NoBooksFoundException();
        }
        List<Map<String, Object>> formattedBooks = formatBooks(booksPage.getContent());
        return formattedBooks;
    }

    public String searchByGenre(String genreName, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page - 1, pageSize);

        Page<Book> bookPage = bookRepository.findByGenreName(genreName, pageable);

        if (bookPage.isEmpty()) {
            throw new NoBooksFoundException();
        }

        List<Map<String, Object>> formattedBooks = formatBooks(bookPage.getContent());

        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(formattedBooks);
        } catch (IOException e) {
            throw new RuntimeException("Could not convert retrieved books to JSON.", e);
        }
    }

    public String searchByYear(int fromYear, int toYear, int page, int pageSize) {
        if (fromYear <= 0 || toYear <= 0) {
            throw new InvalidYearException();
        }
        if (fromYear > toYear) {
            throw new InvalidYearRangeException();
        }

        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<Book> bookPage = bookRepository.findByYearBetween(fromYear, toYear, pageable);

        if (bookPage.isEmpty()) {
            throw new NoBooksFoundException();
        }

        List<Map<String, Object>> formattedBooks = formatBooks(bookPage.getContent());

        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(formattedBooks);
        } catch (IOException e) {
            throw new RuntimeException("Could not convert retrieved books to JSON.", e);
        }
    }

    public List<Map<String, Object>> searchBooks(String title, String authorName, String genre,
                            Integer fromYear, Integer toYear,
                            String sortBy, String sortOrder,
                            int page, int pageSize) {

        if (fromYear != null && fromYear < 0 || toYear != null && toYear < 0) {
            throw new InvalidYearException();
        }
        if (fromYear != null && toYear != null && fromYear > toYear) {
            throw new InvalidYearRangeException();
        }
        List<Book> books = bookRepository.searchBooksWithFilters(
            title, authorName, genre, fromYear, toYear
        );

        if (sortBy != null && !sortBy.isEmpty()) {
            if (sortBy.equalsIgnoreCase("averageRating")) {
                books = sortBooksByAverageRating(books, sortOrder);
            } else if (sortBy.equalsIgnoreCase("reviewCount")) {
                books = sortBooksByReviewCount(books, sortOrder);
            }
        }

        List<Book> paginatedBooks = PaginationUtils.paginate(books, page, pageSize);
        List<Map<String, Object>> formattedBooks = shortFormatBooks(paginatedBooks);

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("books", formattedBooks);
        responseData.put("search", buildSearchString(title, authorName, genre, fromYear, toYear, sortBy, sortOrder));

        return formattedBooks;
    }

    private static String buildSearchString(String title, String authorName, String genre, Integer fromYear, Integer toYear, String sortBy, String sortOrder) {
        List<String> parts = new ArrayList<>();

        if (title != null && !title.isBlank()) {
            parts.add("title:" + title);
        }
        if (authorName != null && !authorName.isBlank()) {
            parts.add("author:" + authorName);
        }
        if (genre != null && !genre.isBlank()) {
            parts.add("genre:" + genre);
        }
        if (fromYear != null && toYear != null) {
            parts.add("year:" + fromYear + "-" + toYear);
        }
        if (sortBy != null && !sortBy.isBlank()) {
            parts.add("sortBy:" + sortBy);
        }
        if (sortOrder != null && !sortOrder.isBlank()) {
            parts.add("sortOrder:" + sortOrder);
        }

        return String.join(", ", parts);
    }

    private List<Book> sortBooksByAverageRating(List<Book> books, String sortOrder) {
        Map<Book, Double> ratingMap = books.stream()
            .collect(Collectors.toMap(
                book -> book,
                book -> {
                    Double avg = reviewRepository.calculateAverageRatingByBook(book);
                    return avg != null ? avg : 0.0;
                }
            ));

        Comparator<Book> comparator = Comparator.comparing(ratingMap::get);
        if ("desc".equalsIgnoreCase(sortOrder)) {
            comparator = comparator.reversed();
        }

        return books.stream()
            .sorted(comparator)
            .collect(Collectors.toList());
    }

    private List<Book> sortBooksByReviewCount(List<Book> books, String sortOrder) {
        Map<Book, Long> countMap = books.stream()
            .collect(Collectors.toMap(book -> book, book -> reviewRepository.countByBook(book)));

        Comparator<Book> comparator = Comparator.comparing(countMap::get);
        if ("desc".equalsIgnoreCase(sortOrder)) {
            comparator = comparator.reversed();
        }

        return books.stream()
            .sorted(comparator)
            .collect(Collectors.toList());
    }

    public List<Map<String, Object>> newRelease() {
        List<Book> books = bookRepository.findTop5ByOrderByYearDesc();
        return shortFormatBooks(books);
    }

    public List<Map<String, Object>> topRated(int page, int pageSize){
        List<Book> books = bookRepository.findAll();
        books = sortBooksByAverageRating(books, "desc");
        List<Book> paginatedBooks = PaginationUtils.paginate(books, page, pageSize);
        return shortFormatBooks(paginatedBooks);
    }
    
    public List<Map<String, Object>> adminPageBooks(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<Book> bookPage = bookRepository.findAll(pageable);
        List<Book> books = bookPage.getContent();

        return adminPageFormatBooks(books);
    }
}
package ir.ac.ut.ece.ie.miobook.service;

import ir.ac.ut.ece.ie.miobook.exceptions.*;
import ir.ac.ut.ece.ie.miobook.model.*;
import ir.ac.ut.ece.ie.miobook.repository.*;
import jakarta.transaction.Transactional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;
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

    public BookService(UserRepository userRepository, BookRepository bookRepository,
                        UserBookRepository userBookRepository,
                        AuthorRepository authorRepository, GenreRepository genreRepository,
                        ReviewRepository reviewRepository) {
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
        this.userBookRepository = userBookRepository;
        this.authorRepository = authorRepository;
        this.genreRepository = genreRepository;
        this.reviewRepository = reviewRepository;
    }

    public void addBook(User user, String title, String authorName, String publisher, int year, int price,
                    String genres, String synopsis, String content) {


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
        if (year < author.getBorn().getYear()) {
            throw new AuthorLifespanException();
        }
        if(author.getDied() != null){
            if(year < author.getDied().getYear()){
                throw new AuthorLifespanException();
            }
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
    public void borrowBook(User user, String title, int days) {
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
    public void addReview(User user, String title, int rate, String comment) {
        if (user.getRole() != Role.CUSTOMER) {
            throw new PermissionDeniedException(Role.ADMIN.toString());
        }

        Customer customer = userRepository.findCustomerByUsername(user.getUsername())
            .orElseThrow(() -> new RuntimeException("Customer not found"));
        
        Book book = bookRepository.findById(title)
                .orElseThrow(() -> new BookNotFoundException(title));

        boolean flag = false;
        for (UserBook borrowedBook : userBookRepository.findBorrowedBooksByCustomer(customer)) {
            if (borrowedBook.getBook().getTitle() == title){
                    flag = true;
                    break;
            } 
        }
        if(!flag){
            for (UserBook userBook : userBookRepository.findOwnedBooksByCustomer(customer)) {
                if (userBook.getBook().getTitle() == title){
                    flag = true;
                        break;
                }
            }
        
        }
        if(!flag){
            throw new BookNotOwnedException();
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

    public Map<String, Object> showBookContent(User user, String title) {
        Book book = bookRepository.findById(title)
                .orElseThrow(() -> new BookNotFoundException(title));

        if (user.getRole() != Role.CUSTOMER) {
            Map<String, Object> data = new HashMap<>();
            data.put("title", book.getTitle());
            data.put("author", book.getAuthor().getName());
            data.put("content", book.getContent());
            return data;
        }

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
        data.put("length", reviewPage.getTotalElements());
        data.put("totalPages", reviewPage.getTotalPages());
        data.put("currentPage", page);
        data.put("pageSize", pageSize);
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
            formattedBook.put("buycount", userBookRepository.countByBook(book));
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
            return null;
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

        Sort sort = Sort.unsorted();
        if (sortBy != null && !sortBy.isEmpty()) {
            Sort.Direction direction = "desc".equalsIgnoreCase(sortOrder) ? Sort.Direction.DESC : Sort.Direction.ASC;
            if (sortBy.equalsIgnoreCase("averageRating")) {
                sort = JpaSort.unsafe(direction, "COALESCE(AVG(r.rate), 0.0)");
            } else if (sortBy.equalsIgnoreCase("reviewCount")) {
                sort = JpaSort.unsafe(direction, "COUNT(DISTINCT r.id)");
            }
        }

        Pageable pageable = PageRequest.of(page - 1, pageSize, sort);
        Page<Book> bookPage = bookRepository.searchBooksWithFilters(
            title, authorName, genre, fromYear, toYear, pageable
        );

        return shortFormatBooks(bookPage.getContent());
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

    public List<Map<String, Object>> newRelease() {
        List<Book> books = bookRepository.findTop5ByOrderByYearDesc();
        return shortFormatBooks(books);
    }

    public List<Map<String, Object>> topRated(int page, int pageSize){
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<Book> bookPage = bookRepository.findTopRatedBooks(pageable);
        return shortFormatBooks(bookPage.getContent());
    }
    
    public List<Map<String, Object>> adminPageBooks(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<Book> bookPage = bookRepository.findAll(pageable);
        List<Book> books = bookPage.getContent();

        return adminPageFormatBooks(books);
    }
}
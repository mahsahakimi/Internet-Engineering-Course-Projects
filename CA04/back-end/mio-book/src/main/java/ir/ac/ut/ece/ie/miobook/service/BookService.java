package ir.ac.ut.ece.ie.miobook.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import ir.ac.ut.ece.ie.miobook.database.AuthorDatabase;
import ir.ac.ut.ece.ie.miobook.database.BookDatabase;
import ir.ac.ut.ece.ie.miobook.database.ReviewDatabase;
import ir.ac.ut.ece.ie.miobook.database.UserDatabase;
import ir.ac.ut.ece.ie.miobook.exceptions.*;
import ir.ac.ut.ece.ie.miobook.model.*;
import ir.ac.ut.ece.ie.miobook.utils.PaginationUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BookService {

    private final UserDatabase userDatabase;
    private final BookDatabase bookDatabase;
    private final AuthorDatabase authorDatabase;
    private final ReviewDatabase reviewDatabase;

    public BookService(UserDatabase userDatabase, BookDatabase bookDatabase, AuthorDatabase authorDatabase, ReviewDatabase reviewDatabase) {
        this.userDatabase = userDatabase;
        this.bookDatabase = bookDatabase;
        this.authorDatabase = authorDatabase;
        this.reviewDatabase = reviewDatabase;
    }

    public void addBook(String title, String authorName, String publisher, int year, int price, List<String> genres, String synopsis, String content) {
        User user = userDatabase.getLoggedInUser();
        if (user == null) {
            throw new UserNotLoggedInException();
        }
        if (user.getRole() == Role.customer) {
            throw new PermissionDeniedException(Role.customer.toString());
        }
        Author author = authorDatabase.findAuthorByName(authorName);
        if (author == null) {
            throw new AuthorNotFoundException(authorName);
        }
        if (year <= 0) {
            throw new InvalidYearException();
        }
        if ((author.getBorn() != null && author.getBorn().getYear() > year) ||
                (author.getDied() != null && author.getDied().getYear() < year)) {
            throw new AuthorLifespanException();
        }
        Book book = new Book(title, author, publisher, year, genres, price, synopsis, content);
        bookDatabase.addBook(book);
    }

    public void borrowBook(String title, int days) {
        User user = userDatabase.getLoggedInUser();
        if (user == null) {
            throw new UserNotLoggedInException();
        }
        if (user.getRole() != Role.customer) {
            throw new PermissionDeniedException(Role.admin.toString());
        }

        Book book = bookDatabase.findBookByTitle(title);
        if (book == null) {
            throw new BookNotFoundException(title);
        }
        if (days < 1 || days > 9) {
            throw new DaysOutOfRangeException();
        }
        if (user.getCart().stream().anyMatch(cartElement -> cartElement.getBook().equals(book))) {
            throw new BookAlreadyInCartException(title);
        }
        if (user.getOwnedBooks().stream().anyMatch(ownedBook -> ownedBook.equals(book))) {
            throw new BookAlreadyOwnedException(title);
        }
        if (user.getBorrowedBooks().stream().anyMatch(borrowedBook -> borrowedBook.getBook().equals(book) && borrowedBook.canAccessBook())) {
            throw new BookAlreadyBorrowedException(title);
        }

        CartElement newElement = new CartElement(book, days);
        List<CartElement> temp = user.getCart();
        temp.add(newElement);
        user.setCart(temp);
    }

    public void addReview(String title, int rate, String comment) {
        User user = userDatabase.getLoggedInUser();
        if (user == null) {
            throw new UserNotLoggedInException();
        }
        if (user.getRole() != Role.customer) {
            throw new PermissionDeniedException(Role.admin.toString());
        }

        Book book = bookDatabase.findBookByTitle(title);
        if (book == null) {
            throw new BookNotFoundException(title);
        }
        // if (!user.getOwnedBooks().contains(book) && user.getBorrowedBooks().stream().noneMatch(borrowedBook -> borrowedBook.getBook().equals(book) && borrowedBook.canAccessBook())) {
        //     throw new BookNotOwnedException();
        // }
        if (rate < 1 || rate > 5) {
            throw new InvalidRatingException();
        }

        MyLocalDateTime submissionTime = MyLocalDateTime.now();
        Review review = new Review(user.getUsername(), title, rate, comment, submissionTime);
        reviewDatabase.addReview(review);
    }

    public Map<String, Object> getBookDetails(String title) {
        Book book = bookDatabase.findBookByTitle(title);
        if (book == null) {
            throw new BookNotFoundException(title);
        }

        Map<String, Object> bookData = new HashMap<>();
        bookData.put("title", book.getTitle());
        bookData.put("author", book.getAuthor());
        bookData.put("publisher", book.getPublisher());
        bookData.put("genres", book.getGenres());
        bookData.put("year", book.getYear());
        bookData.put("price", book.getPrice());
        bookData.put("synopsis", book.getSynopsis());
        bookData.put("averageRating", reviewDatabase.calculateAverageRating(book));
        return bookData;
    }

    public String showBookContent(String title) {
        User user = userDatabase.getLoggedInUser();
        if (user == null) {
            throw new UserNotLoggedInException();
        }
        if (user.getRole() != Role.customer) {
            throw new PermissionDeniedException(Role.admin.toString());
        }

        Book book = bookDatabase.findBookByTitle(title);
        if (book == null) {
            throw new BookNotFoundException(title);
        }

        boolean hasAccess = false;
        // search in owned books
        List<Book> ownedBooks = user.getOwnedBooks();
        for (Book b : ownedBooks) {
            if (b.getTitle().equals(title)) {
                hasAccess = true;
                break;
            }
        }
        // search in borrowed books
        if (!hasAccess) {
            List<BorrowedBook> borrowedBooks = user.getBorrowedBooks();
            for (BorrowedBook borrowedBook : borrowedBooks) {
                if (borrowedBook.getBook().getTitle().equals(title) && borrowedBook.canAccessBook()) {
                    hasAccess = true;
                    break;
                }
            }
        }

        if (hasAccess) {
            Map<String, Object> data = new HashMap<>();
            data.put("title", book.getTitle());
            data.put("content", book.getContent());

            ObjectMapper mapper = new ObjectMapper();
            try {
                return mapper.writeValueAsString(data);
            } catch (IOException e) {
                throw new RuntimeException("Could not convert book content to JSON.", e);
            }
        }
        throw new BookAccessDeniedException();
    }

    public Map<String, Object> getBookReviews(String title, int page, int pageSize){
        Book book = bookDatabase.findBookByTitle(title);
        List<Review> allReviews = reviewDatabase.getReviewsByBook(book);
        int length = allReviews.size();
        if (book == null) {
            throw new BookNotFoundException(title);
        }
        if (allReviews.isEmpty()) {
            throw new NoReviewsException(title);
        }
        List<Review> paginatedReviews = PaginationUtils.paginate(allReviews, page, pageSize);

        List<Map<String, Object>> outputReviews = paginatedReviews.stream()
                .filter(r -> r.getTitle().equals(title))
                .map(r -> {
                    Map<String, Object> newResponse = new HashMap<>();
                    newResponse.put("username", r.getUsername());
                    newResponse.put("rate", r.getRate());
                    newResponse.put("comment", r.getComment());
                    newResponse.put("date", r.getDate());
                    return newResponse;
                })
                .collect(Collectors.toList());

        Map<String, Object> data = new HashMap<>();
        data.put("length", length);
        data.put("reviews", outputReviews);
        data.put("averageRating", reviewDatabase.calculateAverageRating(book));

        return data;
    }



    public String searchByTitle(String title, int page, int pageSize) {
        List<Book> books = bookDatabase.findBooksByTitle(title);
        if (books.isEmpty()) {
            throw new NoBooksFoundException();
        }
        // Apply pagination
        List<Book> paginatedBooks = PaginationUtils.paginate(books, page, pageSize);

        List<Map<String, Object>> formattedBooks = formatBooks(paginatedBooks);
        // TODO: add page and page size if needed!
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(formattedBooks);
        } catch (IOException e) {
            throw new RuntimeException("Could not convert retrieved books to JSON.", e);
        }
    }

    public String searchByAuthor(String authorName, int page, int pageSize) {
        Author author = authorDatabase.findAuthorByName(authorName);
        if (author == null) {
            throw new AuthorNotFoundException(authorName);
        }
        List<Book> books =  bookDatabase.findBooksByAuthor(authorName);
        if (books.isEmpty()) {
            throw new NoBooksFoundException();
        }

        // Apply pagination
        List<Book> paginatedBooks = PaginationUtils.paginate(books, page, pageSize);

        List<Map<String, Object>> formattedBooks = formatBooks(paginatedBooks);
        // TODO: add page and page size if needed!
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(formattedBooks);
        } catch (IOException e) {
            throw new RuntimeException("Could not convert retrieved books to JSON.", e);
        }
    }

    public String searchByGenre(String genre, int page, int pageSize) {
        List<Book> books = bookDatabase.findBooksByGenre(genre);
        if (books.isEmpty()) {
            throw new NoBooksFoundException();
        }

        // Apply pagination
        List<Book> paginatedBooks = PaginationUtils.paginate(books, page, pageSize);

        List<Map<String, Object>> formattedBooks = formatBooks(paginatedBooks);
        // TODO: add page and page size if needed!
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
        List<Book> books = bookDatabase.findBooksByYear(fromYear, toYear);
        if (books.isEmpty()) {
            throw new NoBooksFoundException();
        }

        // Apply pagination
        List<Book> paginatedBooks = PaginationUtils.paginate(books, page, pageSize);

        List<Map<String, Object>> formattedBooks = formatBooks(paginatedBooks);
        // TODO: add page and page size if needed!
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(formattedBooks);
        } catch (IOException e) {
            throw new RuntimeException("Could not convert retrieved books to JSON.", e);
        }
    }

    public String searchBooks(String title, String authorName, String genre, Integer fromYear, Integer toYear, String sortBy, String sortOrder, int page, int pageSize) {
        if (fromYear <= 0 || toYear <= 0) {
            throw new InvalidYearException();
        }
        if (fromYear > toYear) {
            throw new InvalidYearRangeException();
        }

        List<Book> books = bookDatabase.getBooks();

        // Apply filters
        if (title != null && !title.isEmpty()) {
            books = books.stream().filter(book -> book.getTitle().toLowerCase().contains(title.toLowerCase())).collect(Collectors.toList());
        }
        if (authorName != null && !authorName.isEmpty()) {
            books = books.stream().filter(book -> book.getAuthor().getName().toLowerCase().contains(authorName.toLowerCase())).collect(Collectors.toList());
        }
        if (genre != null && !genre.isEmpty()) {
            books = books.stream().filter(book -> book.getGenres().stream().anyMatch(g -> g.toLowerCase().equals(genre.toLowerCase()))).collect(Collectors.toList());
        }
        if (fromYear != null && toYear != null) {
            books = books.stream().filter(book -> book.getYear() >= fromYear && book.getYear() <= toYear).collect(Collectors.toList());
        }

        // Apply sorting
        if (sortBy != null && !sortBy.isEmpty()) {
            if (sortBy.equalsIgnoreCase("averageRating")) {
                books = sortBooksByAverageRating(books, sortOrder);
            } else if (sortBy.equalsIgnoreCase("reviewCount")) {
                books = sortBooksByReviewCount(books, sortOrder);
            }
        }

        // Apply pagination
        List<Book> paginatedBooks = PaginationUtils.paginate(books, page, pageSize);

        // Format books for output
        List<Map<String, Object>> formattedBooks = formatBooks(paginatedBooks);

        // Build response
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("books", formattedBooks);
        responseData.put("search", buildSearchString(title, authorName, genre, fromYear, toYear, sortBy, sortOrder));
        // TODO: add page and page size if needed!

        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(responseData);
        } catch (IOException e) {
            throw new RuntimeException("Could not convert retrieved books to JSON.", e);
        }
    }

    private List<Book> sortBooksByAverageRating(List<Book> books, String sortOrder) {
        return books.stream().sorted((book1, book2) -> {
            double rating1 = reviewDatabase.calculateAverageRating(book1);
            double rating2 = reviewDatabase.calculateAverageRating(book2);
            return sortOrder.equalsIgnoreCase("asc") ? Double.compare(rating1, rating2) : Double.compare(rating2, rating1);
        }).collect(Collectors.toList());
    }

    private List<Book> sortBooksByReviewCount(List<Book> books, String sortOrder) {
        return books.stream().sorted((book1, book2) -> {
            int count1 = reviewDatabase.getReviewsByBook(book1).size();
            int count2 = reviewDatabase.getReviewsByBook(book2).size();
            return sortOrder.equalsIgnoreCase("asc") ? Integer.compare(count1, count2) : Integer.compare(count2, count1);
        }).collect(Collectors.toList());
    }

    private List<Map<String, Object>> formatBooks(List<Book> books) {
        List<Map<String, Object>> formattedBooks = new ArrayList<>();
        for (Book book : books) {
            Map<String, Object> formattedBook = new HashMap<>();
            formattedBook.put("title", book.getTitle());
            formattedBook.put("author", book.getAuthor().getName());
            formattedBook.put("publisher", book.getPublisher());
            formattedBook.put("genres", book.getGenres());
            formattedBook.put("year", book.getYear());
            formattedBook.put("price", book.getPrice());
            formattedBook.put("synopsis", book.getSynopsis());
            formattedBook.put("averageRating", reviewDatabase.calculateAverageRating(book));
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
            formattedBook.put("averageRating", reviewDatabase.calculateAverageRating(book));
            formattedBooks.add(formattedBook);
        }
        return formattedBooks;
    }

    private static String buildSearchString(String title, String authorName, String genre, Integer fromYear, Integer toYear, String sortBy, String sortOrder) {
        StringBuilder searchString = new StringBuilder();
        if (title != null) searchString.append("title:").append(title).append(", ");
        if (authorName != null) searchString.append("author:").append(authorName).append(", ");
        if (genre != null) searchString.append("genre:").append(genre).append(", ");
        if (fromYear != null && toYear != null) searchString.append("year:").append(fromYear).append("-").append(toYear).append(", ");
        if (sortBy != null) searchString.append("sortBy:").append(sortBy).append(", ");
        if (sortOrder != null) searchString.append("sortOrder:").append(sortOrder).append(", ");
        if (searchString.length() > 2) searchString.setLength(searchString.length() - 2); // Remove trailing comma and space
        return searchString.toString();
    }

    public List<Map<String, Object>> newRelease() {
        List<Book> books =  bookDatabase.getBooks();
        books = books.stream()
            .sorted((b1, b2) -> Integer.compare(b2.getYear(), b1.getYear())) // Newest first
            .limit(5)
            .collect(Collectors.toList());
        return shortFormatBooks(books);
    }

    public List<Map<String, Object>> topRated(int page, int pageSize){
        List<Book> books =  bookDatabase.getBooks();
        books = sortBooksByAverageRating(books, "dec");
        return shortFormatBooks(PaginationUtils.paginate(books, page, pageSize));
    }
    
}
package ir.ac.ut.ece.ie.miobook.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import ir.ac.ut.ece.ie.miobook.database.BookDatabase;
import ir.ac.ut.ece.ie.miobook.database.UserDatabase;
import ir.ac.ut.ece.ie.miobook.exceptions.*;
import ir.ac.ut.ece.ie.miobook.model.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CartService {

    private final UserDatabase userDatabase;
    private final BookDatabase bookDatabase;

    public CartService(UserDatabase userDatabase, BookDatabase bookDatabase) {
        this.userDatabase = userDatabase;
        this.bookDatabase = bookDatabase;
    }

    public void addToCart(String username, String title) {
        User user = userDatabase.findUserByUsername(username);
        Book book = bookDatabase.findBookByTitle(title);

        if (user == null) {
            throw new UserNotFoundException(username);
        }
        if (book == null) {
            throw new BookNotFoundException(title);
        }
        if (user.getRole() == Role.admin) {
            throw new PermissionDeniedException(Role.admin.toString());
        }
        if (user.getCart().size() >= 10) {
            throw new CartIsFull();
        }
        if (user.getCart().stream().anyMatch(cartElement -> cartElement.getBook().equals(book))) {
            throw new BookAlreadyInCart(title);
        }
        if (user.getOwnedBooks().stream().anyMatch(ownedBook -> ownedBook.equals(book))) {
            throw new BookAlreadyOwned(title);
        }
        if (user.getBorrowedBooks().stream().anyMatch(borrowedBook -> borrowedBook.getBook().equals(book) && borrowedBook.canAccessBook())) {
            throw new BookAlreadyBorrowed(title);
        }
        CartElement newElement = new CartElement(book, 0);
        user.getCart().add(newElement);
    }

    public void removeFromCart(String username, String title) {
        User user = userDatabase.findUserByUsername(username);
        Book book = bookDatabase.findBookByTitle(title);

        if (user == null) {
            throw new UserNotFoundException(username);
        }
        if (book == null) {
            throw new BookNotFoundException(title);
        }
        if (user.getRole() == Role.admin) {
            throw new PermissionDeniedException(Role.admin.toString());
        }

        boolean found = false;
        for (CartElement ce : user.getCart()) {
            if (ce.getBook().getTitle().equals(title)) {
                found = true;
                break;
            }
        }
        if (!found) {
            throw new BookNotInCart(title);
        }
        user.removeBookFromCart(book);
    }

    public String purchaseCart(String username) {
        User user = userDatabase.findUserByUsername(username);

        if (user == null) {
            throw new UserNotFoundException(username);
        }
        if (user.getRole() == Role.admin) {
            throw new PermissionDeniedException(Role.admin.toString());
        }

        List<CartElement> cart = user.getCart();

        if (cart.isEmpty()) {
            throw new CartIsEmpty();
        }

        int totalCost = 0;
        int cartSize = cart.size();
        for (CartElement ce : cart) {
            totalCost += ce.getPrice();
        }

        if (user.getBalance() < totalCost) {
            throw new InsufficientBalance(username);
        }

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = now.format(formatter);

        user.setBalance(user.getBalance() - totalCost);
        List<String> bookTitles = new ArrayList<>();
        List<Book> ownedBooks = user.getOwnedBooks();
        List<BorrowedBook> borrowedBooks = user.getBorrowedBooks();
        for(CartElement ce : cart){
            bookTitles.add(ce.getBook().getTitle());
            if (ce.getDays() == 0){
                ownedBooks.add(ce.getBook());
            } else {
                LocalDateTime dueDate = now.plusDays(ce.getDays());
                BorrowedBook borrowedBook = new BorrowedBook(ce.getBook(), dueDate);
                borrowedBooks.add(borrowedBook);
            }
        }

        Purchase purchase = new Purchase(bookTitles, totalCost, formattedDateTime);
        user.getPurchases().add(purchase);
        user.getCart().clear();

        Map<String, Object> data = new HashMap<>();
        data.put("bookCount", cartSize);
        data.put("totalCost", totalCost);
        data.put("date", formattedDateTime);

        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(data);
        } catch (IOException e) {
            throw new RuntimeException("Could not convert purchase details to JSON.", e);
        }
    }

    public String showCart(String username) {
        User user = userDatabase.findUserByUsername(username);

        if (user == null) {
            throw new UserNotFoundException(username);
        }
        if (user.getRole() == Role.admin) {
            throw new PermissionDeniedException(Role.admin.toString());
        }

        int totalCost = 0;
        List<Map<String, Object>> items = new ArrayList<>();

        for (CartElement cartElement : user.getCart()) {
            Book book = cartElement.getBook();
            int days = cartElement.getDays();
            int finalPrice = cartElement.getPrice();

            totalCost += finalPrice;

            Map<String, Object> item = new HashMap<>();
            item.put("title", book.getTitle());
            item.put("author", book.getAuthor().getName());
            item.put("publisher", book.getPublisher());
            item.put("genres", book.getGenres());
            item.put("year", book.getYear());
            item.put("price", book.getPrice());
            item.put("isBorrowed", days != 0);
            item.put("finalPrice", finalPrice);
            if (days != 0) {
                item.put("borrowDays", days);
            }
            items.add(item);
        }

        Map<String, Object> data = new HashMap<>();
        data.put("username", username);
        data.put("totalCost", totalCost);
        data.put("items", items);

        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(data);
        } catch (IOException e) {
            throw new RuntimeException("Could not convert cart details to JSON.", e);
        }
    }
}
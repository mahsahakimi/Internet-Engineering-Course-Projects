package ir.ac.ut.ece.ie.miobook.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import ir.ac.ut.ece.ie.miobook.database.BookDatabase;
import ir.ac.ut.ece.ie.miobook.database.UserDatabase;
import ir.ac.ut.ece.ie.miobook.exceptions.*;
import ir.ac.ut.ece.ie.miobook.model.*;
import ir.ac.ut.ece.ie.miobook.validator.AddressValidator;
import ir.ac.ut.ece.ie.miobook.validator.EmailValidator;
import ir.ac.ut.ece.ie.miobook.validator.PasswordValidator;
import ir.ac.ut.ece.ie.miobook.validator.UsernameValidator;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    private final UserDatabase userDatabase;
    private final BookDatabase bookDatabase;

    public UserService(UserDatabase userDatabase, BookDatabase bookDatabase) {
        this.userDatabase = userDatabase;
        this.bookDatabase = bookDatabase;
    }

    public String getPurchasedBooks() throws UserNotFoundException {
        User user = userDatabase.getLoggedInUser();
        if (user == null) {
            throw new UserNotLoggedInException();
        }
        if (user.getRole() == Role.admin) {
            throw new PermissionDeniedException(Role.admin.toString());
        }

        List<Map<String, Object>> books = new ArrayList<>();
        // Add owned books
        for (Book book : user.getOwnedBooks()) {
            Map<String, Object> bookData = new HashMap<>();
            bookData.put("title", book.getTitle());
            bookData.put("author", book.getAuthor().getName());
            bookData.put("publisher", book.getPublisher());
            bookData.put("category", book.getGenres());
            bookData.put("year", book.getYear());
            bookData.put("price", book.getPrice());
            bookData.put("isBorrowed", false);
            books.add(bookData);
        }

        // Add borrowed books that are not expired
        for (BorrowedBook borrowedBook : user.getBorrowedBooks()) {
            if (borrowedBook.canAccessBook()) {
                Book book = borrowedBook.getBook();
                Map<String, Object> bookData = new HashMap<>();
                bookData.put("title", book.getTitle());
                bookData.put("author", book.getAuthor().getName());
                bookData.put("publisher", book.getPublisher());
                bookData.put("category", book.getGenres());
                bookData.put("year", book.getYear());
                bookData.put("price", book.getPrice());
                bookData.put("isBorrowed", true);
                books.add(bookData);
            }
        }

        Map<String, Object> data = new HashMap<>();
        data.put("username", user.getUsername());
        data.put("books", books);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Purchased books retrieved successfully.");
        response.put("data", data);

        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(response);
        } catch (IOException e) {
            throw new RuntimeException("Could not convert purchased books to JSON.", e);
        }
    }

    public String getBookStatus(String title) {
        User user = userDatabase.getLoggedInUser();
        if (user == null) {
            throw new UserNotLoggedInException();
        }
        if (user.getRole() == Role.admin) {
            throw new PermissionDeniedException(Role.admin.toString());
        }
        String status = null;
        for (Book book : user.getOwnedBooks()) {
            if(book.getTitle() == title){
                status = "buy";
                return status;
            }
        }
        for (BorrowedBook borrowedBook : user.getBorrowedBooks()) {
            if (borrowedBook.canAccessBook()) {
                Book book = borrowedBook.getBook();
                if(book.getTitle() == title){
                    status = "borrow";
                    return status;
                }
            }
        }
        Book book = bookDatabase.findBookByTitle(title);
        if (user.getCart().stream().anyMatch(cartElement -> cartElement.getBook().equals(book))) {
            status = "cart";
            return status;
        }
        return status;
    }

    public String getPurchaseHistory() {
        User user = userDatabase.getLoggedInUser();
        if (user == null) {
            throw new UserNotLoggedInException();
        }
        if (user.getRole() == Role.admin) {
            throw new PermissionDeniedException(Role.admin.toString());
        }

        List<Map<String, Object>> purchaseHistory = new ArrayList<>();

        for (Purchase purchase : user.getPurchases()) {
            Map<String, Object> purchaseData = new HashMap<>();

            MyLocalDateTime purchaseDateTime = MyLocalDateTime.parse(purchase.getPurchaseDateTime());
            purchaseData.put("purchaseDate", purchaseDateTime);

            List<Map<String, Object>> items = new ArrayList<>();
            for (String title : purchase.getBookTitles()) {
                Book book = bookDatabase.findBookByTitle(title);
                if (book != null) {
                    Map<String, Object> item = new HashMap<>();
                    item.put("title", book.getTitle());
                    item.put("author", book.getAuthor().getName());
                    item.put("publisher", book.getPublisher());
                    item.put("genres", book.getGenres());
                    item.put("year", book.getYear());
                    item.put("price", book.getPrice());

                    boolean isBorrowed = false;
                    int borrowDays = 0;
                    int finalPrice = book.getPrice();

                    // Check if the book was borrowed
                    for (BorrowedBook borrowedBook : user.getBorrowedBooks()) {
                        if (borrowedBook.getBook().getTitle().equals(book.getTitle())) {
                            isBorrowed = true;
                            MyLocalDateTime borrowDueDate = borrowedBook.getDueDate();
                            MyLocalDateTime purchaseDate = MyLocalDateTime.parse(purchase.getPurchaseDateTime());
                            borrowDays = (int) MyLocalDateTime.daysBetween(purchaseDate, borrowDueDate);
                            finalPrice = (book.getPrice() * borrowDays) / 10;
                            break;
                        }
                    }

                    item.put("isBorrowed", isBorrowed);
                    if (isBorrowed) {
                        item.put("borrowDays", borrowDays);
                    }
                    item.put("finalPrice", finalPrice);
                    items.add(item);
                }
            }
            purchaseData.put("items", items);
            purchaseData.put("totalCost", purchase.getTotalCost());
            purchaseHistory.add(purchaseData);
        }

        Map<String, Object> data = new HashMap<>();
        data.put("username", user.getUsername());
        data.put("purchaseHistory", purchaseHistory);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Purchase history retrieved successfully.");
        response.put("data", data);

        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(response);
        } catch (IOException e) {
            throw new RuntimeException("Could not convert purchase history to JSON.", e);
        }
    }

    public String getUserDetails() {
        User user = userDatabase.getLoggedInUser();
        if (user == null) {
            throw new UserNotLoggedInException();
        }

        Map<String, Object> userData = new HashMap<>();
        userData.put("username", user.getUsername());
        userData.put("role", user.getRole());
        userData.put("email", user.getEmail());
        userData.put("address", user.getAddress());

        if (user.getRole() == Role.customer) {
            userData.put("balance", user.getBalance());
        }

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "User details retrieved successfully.");
        response.put("data", userData);

        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(response);
        } catch (IOException e) {
            throw new RuntimeException("Could not convert user details to JSON.", e);
        }
    }

    public int addCredit(int credit) {
        User user = userDatabase.getLoggedInUser();
        if (user == null) {
            throw new UserNotLoggedInException();
        }
        if (user.getRole() != Role.customer) {
            throw new PermissionDeniedException(Role.admin.toString());
        }
        if (credit <= 0) {
            throw new InvalidCreditAmountException();
        }
        if (credit < 100) {
            throw new MinimumCreditException();
        }

        user.setBalance(user.getBalance() + credit);
        return user.getBalance();
    }

    public void addUser(String role, String username, String password, String email, Address address) {
        try {
            Role.valueOf(role);
        } catch (IllegalArgumentException e) {
            throw new InvalidRoleException(role);
        }

        if (!UsernameValidator.isValid(username)) {
            throw new InvalidUsernameException(username);
        }

        if (!PasswordValidator.isValid(password)) {
            throw new InvalidPasswordException();
        }

        if (!EmailValidator.isValid(email)) {
            throw new InvalidEmailException(email);
        }

        if (!AddressValidator.isValid(address.getCountry(), address.getCity())) {
            throw new InvalidAddressException(address.getCountry(), address.getCity());
        }

        User user = new User(role, username, password, email, address);
        userDatabase.addUser(user);
    }
}
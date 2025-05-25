package ir.ac.ut.ece.ie.miobook.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import ir.ac.ut.ece.ie.miobook.database.BookDatabase;
import ir.ac.ut.ece.ie.miobook.database.UserDatabase;
import ir.ac.ut.ece.ie.miobook.exceptions.*;
import ir.ac.ut.ece.ie.miobook.model.*;
import ir.ac.ut.ece.ie.miobook.repository.UserRepository;
import ir.ac.ut.ece.ie.miobook.validator.AddressValidator;
import ir.ac.ut.ece.ie.miobook.validator.EmailValidator;
import ir.ac.ut.ece.ie.miobook.validator.PasswordValidator;
import ir.ac.ut.ece.ie.miobook.validator.UsernameValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserDatabase userDatabase;
    private final BookDatabase bookDatabase;

    @Autowired
    public UserService(UserRepository userRepository, UserDatabase userDatabase, BookDatabase bookDatabase) {
        this.userRepository = userRepository;
        this.userDatabase = userDatabase;
        this.bookDatabase = bookDatabase;
    }

    public Map<String, Object> getPurchasedBooks() {
        User user = userDatabase.getLoggedInUser();
        if (user == null) {
            throw new UserNotLoggedInException();
        }
        if (user.getRole() == Role.ADMIN) {
            throw new PermissionDeniedException(Role.ADMIN.toString());
        }
        Customer customer = userRepository.findCustomerByUsername(user.getUsername()).orElse(null);
        Map<String, Object> userBooks = new HashMap<>();
    
        List<Map<String, Object>> borrowedBooksInfo = new ArrayList<>();
        for (BorrowedBook borrowedBook : customer.getBorrowedBooks()) {
            if (borrowedBook.canAccessBook()) {
                Book book = borrowedBook.getBook();
                Map<String, Object> bookInfo = new HashMap<>();
                bookInfo.put("title", book.getTitle());
                bookInfo.put("author", book.getAuthor().getName());
                bookInfo.put("genres", book.getGenres());
                bookInfo.put("publisher", book.getPublisher());
                bookInfo.put("year", book.getYear());
                bookInfo.put("dueDate", borrowedBook.getDueDate().toString());
                borrowedBooksInfo.add(bookInfo);
            }
        }
        userBooks.put("borrowed", borrowedBooksInfo);
    
        List<Map<String, Object>> ownedBooksInfo = new ArrayList<>();
        for (Book book : customer.getOwnedBooks()) {
            Map<String, Object> bookInfo = new HashMap<>();
            bookInfo.put("title", book.getTitle());
            bookInfo.put("author", book.getAuthor().getName());
            bookInfo.put("genres", book.getGenres());
            bookInfo.put("publisher", book.getPublisher());
            bookInfo.put("year", book.getYear());
            ownedBooksInfo.add(bookInfo);
        }
        userBooks.put("owned", ownedBooksInfo);
    
        return userBooks;
    }
    
    public String getBookStatus(String title) {
        User user = userDatabase.getLoggedInUser();
        if (user == null) {
            throw new UserNotLoggedInException();
        }
        if (user.getRole() == Role.ADMIN) {
            throw new PermissionDeniedException(Role.ADMIN.toString());
        }
        Customer customer = userRepository.findCustomerByUsername(user.getUsername()).orElse(null);
        String status = null;
        for (Book book : customer.getOwnedBooks()) {
            if(book.getTitle().equals(title)){
                status = "buy";
                return status;
            }
        }
            
        for (BorrowedBook borrowedBook : customer.getBorrowedBooks()) {
            if (borrowedBook.canAccessBook()) {
                Book book = borrowedBook.getBook();
                if(book.getTitle().equals(title)){
                    status = "borrow";
                    return status;
                }
            }
        }
        Book book = bookDatabase.findBookByTitle(title);
        if (customer.getCart().stream().anyMatch(cartElement -> cartElement.getBook().equals(book))) {
            status = "cart";
            return status;
        }
        return status;
    }

    public Map<String, Object> getPurchaseHistory() {
        User user = userDatabase.getLoggedInUser();
        if (user == null) {
            throw new UserNotLoggedInException();
        }
        if (user.getRole() == Role.ADMIN) {
            throw new PermissionDeniedException(Role.ADMIN.toString());
        }
        Customer customer = userRepository.findCustomerByUsername(user.getUsername()).orElse(null);

        List<Map<String, Object>> purchaseHistory = new ArrayList<>();
        for (Purchase purchase : customer.getPurchases()) {
            Map<String, Object> purchaseData = new HashMap<>();

            MyLocalDateTime purchaseDateTime = MyLocalDateTime.parse(purchase.getPurchaseDateTime());
            purchaseData.put("date", purchaseDateTime);

            List<Map<String, Object>> items = new ArrayList<>();
            for (String title : purchase.getBookTitles()) {
                Book book = bookDatabase.findBookByTitle(title);
                if (book != null) {
                    Map<String, Object> item = new HashMap<>();
                    item.put("title", book.getTitle());
                    item.put("author", book.getAuthor().getName());
                    item.put("price", book.getPrice());

                    boolean isBorrowed = false;
                    int borrowDays = 0;
                    int finalPrice = book.getPrice();

                    // Check if the book was borrowed
                    for (BorrowedBook borrowedBook : customer.getBorrowedBooks()) {
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
        data.put("username", customer.getUsername());
        data.put("purchaseHistory", purchaseHistory);

        return data;
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

        if (user.getRole() == Role.CUSTOMER) {
            Customer customer = userRepository.findCustomerByUsername(user.getUsername()).orElse(null);
            userData.put("balance", customer.getBalance());
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
        if (user.getRole() != Role.CUSTOMER) {
            throw new PermissionDeniedException(Role.ADMIN.toString());
        }
        if (credit <= 0) {
            throw new InvalidCreditAmountException();
        }
        if (credit < 100) {
            throw new MinimumCreditException();
        }
        Customer customer = userRepository.findCustomerByUsername(user.getUsername()).orElse(null);

        customer.setBalance(customer.getBalance() + credit);
        return customer.getBalance();
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

        User user;
        Role parsedRole = Role.valueOf(role.toUpperCase());
        if (parsedRole == Role.ADMIN) {
            user = new Admin(username, password, email, address);
        } else { // Role.CUSTOMER
            user = new Customer(username, password, email, address);
        }

        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            // Handle potential unique constraint violations for username or email
            if (e.getMessage().contains("unique constraint") && e.getMessage().contains(username)) {
                throw new UsernameAlreadyExistsException(username);
            } else if (e.getMessage().contains("unique constraint") && e.getMessage().contains(email)) {
                throw new EmailAlreadyExistsException(email);
            }
            throw new RuntimeException("Error saving user: " + e.getMessage());
        }
    }
}
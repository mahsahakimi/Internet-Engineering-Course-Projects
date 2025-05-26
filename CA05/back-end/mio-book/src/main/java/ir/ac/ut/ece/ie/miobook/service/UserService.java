package ir.ac.ut.ece.ie.miobook.service;

import ir.ac.ut.ece.ie.miobook.context.UserContext;
import ir.ac.ut.ece.ie.miobook.exceptions.*;
import ir.ac.ut.ece.ie.miobook.model.*;
import ir.ac.ut.ece.ie.miobook.repository.BookRepository;
import ir.ac.ut.ece.ie.miobook.repository.UserRepository;
import ir.ac.ut.ece.ie.miobook.repository.WalletRepository;
import ir.ac.ut.ece.ie.miobook.validator.AddressValidator;
import ir.ac.ut.ece.ie.miobook.validator.EmailValidator;
import ir.ac.ut.ece.ie.miobook.validator.PasswordValidator;
import ir.ac.ut.ece.ie.miobook.validator.UsernameValidator;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;




@Service
public class UserService {

    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final WalletRepository walletRepository;
    private final UserContext userContext;

    @Autowired
    public UserService(UserRepository userRepository, BookRepository bookRepository,
                        WalletRepository walletRepository, UserContext userContext) {
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
        this.walletRepository = walletRepository;
        this.userContext = userContext;
    }
    public Map<String, Object> getPurchasedBooks() {
        User user = userContext.getLoggedInUser();
        if (user == null) {
            throw new UserNotLoggedInException();
        }
        if (user.getRole() == Role.ADMIN) {
            throw new PermissionDeniedException(Role.ADMIN.toString());
        }

        Customer customer = (Customer) user;

        Map<String, Object> userBooks = new HashMap<>();

        // Borrowed books
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

        // Owned books
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
        User user = userContext.getLoggedInUser();
        if (user == null) {
            throw new UserNotLoggedInException();
        }
        if (user.getRole() == Role.ADMIN) {
            throw new PermissionDeniedException(Role.ADMIN.toString());
        }

        Customer customer = userRepository.findCustomerByUsername(user.getUsername())
                .orElseThrow(UserNotLoggedInException::new);

        for (Book book : customer.getOwnedBooks()) {
            if (book.getTitle().equalsIgnoreCase(title)) {
                return "buy";
            }
        }

        for (BorrowedBook borrowedBook : customer.getBorrowedBooks()) {
            if (borrowedBook.canAccessBook() &&
                borrowedBook.getBook().getTitle().equalsIgnoreCase(title)) {
                return "borrow";
            }
        }

        for (CartElement element : customer.getCart()) {
            if (element.getBook().getTitle().equalsIgnoreCase(title)) {
                return "cart";
            }
        }

        return null;
    }

    public Map<String, Object> getPurchaseHistory() {
        User user = userContext.getLoggedInUser();
        if (user == null) {
            throw new UserNotLoggedInException();
        }
        if (user.getRole() == Role.ADMIN) {
            throw new PermissionDeniedException(Role.ADMIN.toString());
        }

        Customer customer = userRepository.findCustomerByUsername(user.getUsername())
                .orElseThrow(() -> new UserNotLoggedInException());

        List<Map<String, Object>> purchaseHistory = new ArrayList<>();

        for (Purchase purchase : customer.getPurchases()) {
            Map<String, Object> purchaseData = new HashMap<>();

            MyLocalDateTime purchaseDateTime = MyLocalDateTime.parse(purchase.getPurchaseDateTime());
            purchaseData.put("date", purchaseDateTime);

            List<Map<String, Object>> items = new ArrayList<>();

            for (String title : purchase.getBookTitles()) {
                Optional<Book> optionalBook = bookRepository.findByTitle(title);
                if (optionalBook.isEmpty()) {
                    continue;
                }
                Book book = optionalBook.get();

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
                        borrowDays = (int) MyLocalDateTime.daysBetween(purchaseDateTime, borrowDueDate);
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
        User user = userContext.getLoggedInUser();
        if (user == null) {
            throw new UserNotLoggedInException();
        }

        Map<String, Object> userData = new HashMap<>();
        userData.put("username", user.getUsername());
        userData.put("role", user.getRole());
        userData.put("email", user.getEmail());
        userData.put("address", user.getAddress());

        if (user.getRole() == Role.CUSTOMER) {
            // Only try to cast to Customer if the role is CUSTOMER
            Customer customer = userRepository.findCustomerByUsername(user.getUsername())
                                            .orElseThrow(UserNotLoggedInException::new);
            userData.put("balance", customer.getWallet().getBalance());
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
        User user = userContext.getLoggedInUser();
        if (user == null) {
            throw new UserNotLoggedInException();
        }
        if (user.getRole() != Role.CUSTOMER) {
            throw new PermissionDeniedException(Role.ADMIN.toString());
        }

        Customer customer = userRepository.findCustomerByUsername(user.getUsername())
                .orElseThrow(UserNotLoggedInException::new);

        Wallet wallet = customer.getWallet();
        if (wallet == null) {
            throw new IllegalStateException("Customer wallet not found.");
        }

        wallet.deposit(credit);

        return wallet.getBalance();
    }

    @Transactional
    public void addUser(String role, String username, String password, String email, Address address) {
        // Validate role
        Role parsedRole;
        try {
            parsedRole = Role.valueOf(role.toUpperCase());
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
        if (parsedRole == Role.ADMIN) {
            user = new Admin(username, password, email, address);
        } else {
            user = new Customer(username, password, email, address);
        }

        try {
            userRepository.save(user);

            // If it's a customer, create wallet
            if (user instanceof Customer customer) {
                Wallet wallet = new Wallet(customer);
                walletRepository.save(wallet);
            }

        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("unique constraint") && e.getMessage().contains(username)) {
                throw new UsernameAlreadyExistsException(username);
            } else if (e.getMessage().contains("unique constraint") && e.getMessage().contains(email)) {
                throw new EmailAlreadyExistsException(email);
            }
            throw new RuntimeException("Error saving user: " + e.getMessage());
        }
    }
}
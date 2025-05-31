package ir.ac.ut.ece.ie.miobook.service;

import ir.ac.ut.ece.ie.miobook.context.UserContext;
import ir.ac.ut.ece.ie.miobook.exceptions.*;
import ir.ac.ut.ece.ie.miobook.model.*;

import ir.ac.ut.ece.ie.miobook.repository.*;
import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CartService {

    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final UserBookRepository userBookRepository;
    private final PurchaseRepository purchaseRepository;
    private final CartElementRepository cartElementRepository;
    private final UserContext userContext;
    private final WalletService walletService;

    public CartService(UserRepository userRepository, BookRepository bookRepository, UserBookRepository userBookRepository, PurchaseRepository purchaseRepository, CartElementRepository cartElementRepository, UserContext userContext, WalletService walletService) {
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
        this.userBookRepository = userBookRepository;
        this.purchaseRepository = purchaseRepository;
        this.cartElementRepository = cartElementRepository;
        this.userContext = userContext;
        this.walletService = walletService;
    }

    public void addToCart(String title, int day) {
        User user = userContext.getLoggedInUser();
        if (user == null) {
            throw new UserNotLoggedInException();
        }
        if (user.getRole() == Role.ADMIN) {
            throw new PermissionDeniedException(Role.ADMIN.toString());
        }
        Customer customer = userRepository.findCustomerByUsername(user.getUsername())
                .orElseThrow(() -> new UserNotFoundException(user.getUsername()));

        Book book = bookRepository.findByTitle(title).orElseThrow(() -> new BookNotFoundException(title));

        if (customer.getCart().size() >= 10) {
            throw new CartIsFullException();
        }
        if (customer.getCart().stream().anyMatch(cartElement -> cartElement.getBook().equals(book))) {
            throw new BookAlreadyInCartException(title);
        }
        List<UserBook> userBooks = userBookRepository.findAllByCustomerAndBook(customer, book);
        for (UserBook userBook : userBooks) {
            if (!userBook.isBorrowed()) {
                throw new BookAlreadyOwnedException(title);
            } else if (userBook.canAccessBook()) {
                throw new BookAlreadyBorrowedException(title);
            }
        }

        CartElement newElement = new CartElement(book, day);
        newElement.setCustomer(customer);
        customer.getCart().add(newElement);
        // userRepository.save(customer);
    }

    @Transactional
    public void removeFromCart(String title) {
        User user = userContext.getLoggedInUser();
        if (user == null) {
            throw new UserNotLoggedInException();
        }
        if (user.getRole() == Role.ADMIN) {
            throw new PermissionDeniedException(Role.ADMIN.toString());
        }
        Customer customer = userRepository.findCustomerByUsername(user.getUsername())
                .orElseThrow(() -> new UserNotFoundException(user.getUsername()));

        Book book = bookRepository.findByTitle(title).orElseThrow(() -> new BookNotFoundException(title));

        boolean found = customer.getCart().removeIf(cartElement -> cartElement.getBook().equals(book));
        if (!found) {
            throw new BookNotInCartException(title);
        }
        customer.removeBookFromCart(book);
    }

    public Purchase purchaseCart() {
        User user = userContext.getLoggedInUser();
        if (user == null) {
            throw new UserNotLoggedInException();
        }
        if (user.getRole() == Role.ADMIN) {
            throw new PermissionDeniedException(Role.ADMIN.toString());
        }
        Customer customer = userRepository.findCustomerByUsername(user.getUsername())
                .orElseThrow(() -> new UserNotFoundException(user.getUsername()));

        List<CartElement> cart = customer.getCart();

        if (cart.isEmpty()) {
            throw new CartIsEmptyException();
        }

        int totalCost = 0;
        for (CartElement ce : cart) {
            totalCost += ce.getPrice();
        }

        if (walletService.getBalance(customer.getUsername()) < totalCost) {
            throw new InsufficientBalanceException(user.getUsername());
        }

        MyLocalDateTime now = MyLocalDateTime.now();

        walletService.withdraw(customer.getUsername(), totalCost);
        List<String> bookTitles = new ArrayList<>();
        for(CartElement ce : cart){
            bookTitles.add(ce.getBook().getTitle());
            if (ce.getDays() == 0){
                UserBook ownedBook = new UserBook(customer, ce.getBook());
                userBookRepository.save(ownedBook);
            } else {
                UserBook borrowedBook = new UserBook(customer, ce.getBook());
                borrowedBook.markAsBorrowed(ce.getDays(), now);
                userBookRepository.save(borrowedBook);
            }
        }

        Purchase purchase = new Purchase(bookTitles, totalCost, now, customer.getUsername());
        customer.getPurchases().add(purchase);
        customer.getCart().clear();
        userRepository.save(customer);
        // it does not need! 
        purchaseRepository.save(purchase); 
        return purchase;
    }

    public Map<String, Object> showCart() {
        User user = userContext.getLoggedInUser();
        if (user == null) {
            throw new UserNotLoggedInException();
        }
        if (user.getRole() == Role.ADMIN) {
            throw new PermissionDeniedException(Role.ADMIN.toString());
        }
        Customer customer = userRepository.findCustomerByUsername(user.getUsername())
                .orElseThrow(() -> new UserNotFoundException(user.getUsername()));

        int totalCost = 0;
        List<Map<String, Object>> items = new ArrayList<>();

        for (CartElement cartElement : customer.getCart()) {
            Book book = cartElement.getBook();
            int days = cartElement.getDays();
            int finalPrice = cartElement.getPrice();

            totalCost += finalPrice;

            Map<String, Object> item = new HashMap<>();
            item.put("title", book.getTitle());
            item.put("author", book.getAuthor().getName());
            item.put("price", book.getPrice());
            item.put("isBorrowed", days != 0);
            item.put("finalPrice", finalPrice);
            if (days != 0) {
                item.put("borrowDays", days);
            }
            items.add(item);
        }

        Map<String, Object> data = new HashMap<>();
        data.put("totalCost", totalCost);
        data.put("items", items);

        return data;
    }
}
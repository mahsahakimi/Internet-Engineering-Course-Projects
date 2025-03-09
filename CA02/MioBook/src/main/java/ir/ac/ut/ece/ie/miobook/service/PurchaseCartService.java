package ir.ac.ut.ece.ie.miobook.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import ir.ac.ut.ece.ie.miobook.database.UserDatabase;
import ir.ac.ut.ece.ie.miobook.model.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PurchaseCartService {

    public static String purchaseCart(String username) throws IOException {
        User user = UserDatabase.getInstance().findUserByUsername(username);

        if (user == null) {
            throw new IllegalArgumentException("User not found.");
        }

        if (user.getRole() == Role.admin) {
            throw new IllegalArgumentException("Permission denied. Admin cannot purchase.");
        }

        List<CartElement> cart = user.getCart();

        if (cart.isEmpty()) {
            throw new IllegalArgumentException("Cart is empty.");
        }

        int totalCost = 0;
        int cartSize = cart.size();
        for (CartElement ce : cart) {
            totalCost += ce.getPrice();
        }

        if (user.getBalance() < totalCost) {
            throw new IllegalArgumentException("Insufficient balance.");
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

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Purchase completed successfully.");
        response.put("data", data);

        return new ObjectMapper().writeValueAsString(response);
    }
}

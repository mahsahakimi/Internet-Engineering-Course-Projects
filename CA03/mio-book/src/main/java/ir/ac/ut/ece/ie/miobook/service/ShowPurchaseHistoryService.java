package ir.ac.ut.ece.ie.miobook.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import ir.ac.ut.ece.ie.miobook.database.BookDatabase;
import ir.ac.ut.ece.ie.miobook.database.UserDatabase;
import ir.ac.ut.ece.ie.miobook.model.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShowPurchaseHistoryService {

    public static String showPurchaseHistory(String username) throws IOException {
        User user = UserDatabase.getInstance().findUserByUsername(username);

        if (user == null) {
            throw new IllegalArgumentException("User not found.");
        }

        if (user.getRole() == Role.admin) {
            throw new IllegalArgumentException("Admins do not have a purchase history.");
        }

        List<Map<String, Object>> purchaseHistory = new ArrayList<>();

        for (Purchase purchase : user.getPurchases()) {
            Map<String, Object> purchaseData = new HashMap<>();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime purchaseDateTime = LocalDateTime.parse(purchase.getPurchaseDateTime(), formatter);
            String formattedDateTime = purchaseDateTime.format(formatter);
            purchaseData.put("purchaseDate", formattedDateTime);

            List<Map<String, Object>> items = new ArrayList<>();
            for (String title : purchase.getBookTitles()) {
                Book book = BookDatabase.getInstance().findBookByTitle(title);
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
                            LocalDateTime borrowDueDate = borrowedBook.getDueDate();
                            LocalDateTime purchaseDate = LocalDateTime.parse(purchase.getPurchaseDateTime(), formatter);
                            borrowDays = (int) java.time.temporal.ChronoUnit.DAYS.between(purchaseDate, borrowDueDate);
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
        data.put("username", username);
        data.put("purchaseHistory", purchaseHistory);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Purchase history retrieved successfully.");
        response.put("data", data);

        return new ObjectMapper().writeValueAsString(response);
    }
}

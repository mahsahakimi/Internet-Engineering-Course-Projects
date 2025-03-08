package ir.ac.ut.ece.ie.miobook.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class User {
    private Role role;
    private String username;
    private String password;
    private String email;
    private Address address;
    private int balance;
    List<CartElement> cart = new ArrayList<>();

    List<Purchase> purchases = new ArrayList<>();
    List<Book> ownedBooks = new ArrayList<>();
//    Map<Book, LocalDate> borrowedBooks = new HashMap<>();

    public User(String role, String username, String password, String email, Address address) {
        this.role = Role.valueOf(role);
        this.username = username;
        this.password = password;
        this.email = email;
        this.address = address;
        this.balance = 0;
    }

    public void addToCart(CartElement book){
        this.cart.add(book);
    }

    public void clearCart() {
        this.cart.clear();
    }

    public void removeBook(Book book){
        this.cart.removeIf(ce -> ce.book.title.equals(book.getTitle()));
    }

//    public void addToPurchase(Purchase p, List<CartElement> cart){
//        LocalDate tempDate;
//        this.purchases.add(p);
//        // add books to user owned books
//        for (CartElement ce : cart){
//            if(ce.getDays() == 0){
//                ownedBooks.add(ce.book);
//            }
//            else{
//                tempDate = LocalDate.now().plusDays(ce.getDays());
//                borrowedBooks.put(ce.getBook(), tempDate);
//            }
//        }
//    }

    @Override
    public String toString() {
        return "User{" +
                "role=" + role +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", address=" + address +
                ", balance=" + balance +
                ", cart=" + cart +
                '}';
    }
}
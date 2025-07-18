package ir.ac.ut.ece.ie.miobook.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "cart_element")
@Getter
@Setter
@NoArgsConstructor
public class CartElement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_username", nullable = false)
    private Customer customer;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @Column(nullable = false)
    private int days;

    @Column(nullable = false)
    private int price;

    public CartElement(Book book, int days) {
        this.book = book;
        this.days = days;
        this.price = calculatePrice(book.getPrice(), days);
    }

    private int calculatePrice(int basePrice, int days) {
        return days == 0 ? basePrice : (basePrice * days) / 10;
    }
}
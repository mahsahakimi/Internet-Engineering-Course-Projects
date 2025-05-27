package ir.ac.ut.ece.ie.miobook.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "purchases")
@Getter
@Setter
@NoArgsConstructor
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ElementCollection
    @CollectionTable(
        name = "purchase_books",
        joinColumns = @JoinColumn(name = "purchase_id")
    )
    @Column(name = "book_title")
    private List<String> bookTitles;

    @Column(nullable = false)
    private int totalCost;

    @Column(name = "purchase_date_time", nullable = false)
    private LocalDateTime purchaseDateTime;

    // FK column in purchase table that references the Customer's username
    @Column(name = "user_username", nullable = false)
    private String userUsername;

    public Purchase(List<String> bookTitles, int totalCost, LocalDateTime purchaseDateTime, String userUsername) {
        this.bookTitles = bookTitles;
        this.totalCost = totalCost;
        this.purchaseDateTime = purchaseDateTime;
        this.userUsername = userUsername;
    }
}

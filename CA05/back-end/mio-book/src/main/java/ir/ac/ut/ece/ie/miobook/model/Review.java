package ir.ac.ut.ece.ie.miobook.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "Review")
@Table(name = "review")
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int rate;

    @Column(columnDefinition = "TEXT")
    private String comment;

    @Column(nullable = false)
    private MyLocalDateTime date;;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_title", nullable = false,
            foreignKey = @ForeignKey(name = "fk_review_book"))
    private Book title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_username", nullable = false,
            foreignKey = @ForeignKey(name = "fk_review_customer"))
    private Customer username;

    public Review(Customer username, Book title, int rate, String comment, MyLocalDateTime date)
    {
        this.title = title;
        this.username = username;
        this.comment = comment;
        this.date = date;
        this.rate = rate;
    }
}

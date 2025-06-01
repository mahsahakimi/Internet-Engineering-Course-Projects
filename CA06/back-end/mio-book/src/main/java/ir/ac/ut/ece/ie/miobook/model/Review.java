package ir.ac.ut.ece.ie.miobook.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ir.ac.ut.ece.ie.miobook.converter.MyLocalDateTimeConverter;
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

    @Convert(converter = MyLocalDateTimeConverter.class)
    @Column(nullable = false)
    private MyLocalDateTime date;;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_title", nullable = false,
            foreignKey = @ForeignKey(name = "fk_review_book"))
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_username", nullable = false,
            foreignKey = @ForeignKey(name = "fk_review_customer"))
    private Customer customer;

    public Review(Customer customer, Book book, int rate, String comment, MyLocalDateTime date)
    {
        this.book = book;
        this.customer = customer;
        this.comment = comment;
        this.date = date;
        this.rate = rate;
    }
}

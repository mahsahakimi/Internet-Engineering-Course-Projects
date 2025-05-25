package ir.ac.ut.ece.ie.miobook.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Set;
import java.util.HashSet;
// import ir.ac.ut.ece.ie.miobook.model.Genre;  // adjust if in same or different package


@Entity
@Table(name = "books", uniqueConstraints = @UniqueConstraint(columnNames = "title"))
@Getter
@Setter
@NoArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String title;

    @ManyToOne(optional = false)
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;

    @Column(nullable = false)
    private String publisher;

    private int year;

    @Column(nullable = false)
    private int price;

    @Column(columnDefinition = "TEXT")
    private String synopsis;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "book_genres",
        joinColumns = @JoinColumn(name = "book_id"),
        inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private Set<Genre> genres = new HashSet<>();

    public Book(String title, Author author, String publisher, int year, int price,
                String synopsis, String content) {
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.year = year;
        this.price = price;
        this.synopsis = synopsis;
        this.content = content;
    }

    // Optional helper to add genres one by one
    public void addGenre(Genre genre) {
        this.genres.add(genre);
        genre.getBooks().add(this);
    }
}
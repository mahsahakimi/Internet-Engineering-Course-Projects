package ir.ac.ut.ece.ie.miobook.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "Author")
@Setter
@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Author {

    @Id
    @Column(name = "name", columnDefinition = "TEXT", unique = true)
    private String name;

    @Column(name = "pen_name", columnDefinition = "TEXT", nullable = false)
    private String penName;

    @Column(name = "nationality", columnDefinition = "TEXT", nullable = false)
    private String nationality;

    @Column(name = "born_date", nullable = false, columnDefinition = "DATE")
    private MyLocalDate born;

    @Column(name = "died_date", nullable = false, columnDefinition = "DATE")
    private MyLocalDate died;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "username",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_author_admin")
    )
    private Admin username;

    public Author(
            String name,
            String penName,
            String nationality,
            MyLocalDate born,
            MyLocalDate died,
            Admin username
    ) {
        this.name = name;
        this.penName = penName;
        this.nationality = nationality;
        this.born = born;
        this.died = died;
        this.username = username;
    }
}
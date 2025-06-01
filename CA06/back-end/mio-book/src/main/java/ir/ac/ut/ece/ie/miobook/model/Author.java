package ir.ac.ut.ece.ie.miobook.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ir.ac.ut.ece.ie.miobook.converter.MyLocalDateConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "Author")
@Table(name = "author")
@Setter
@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Author {

    @Column(name = "name", length = 255, columnDefinition = "VARCHAR(255)")
    @Id
    private String name;

    @Column(name = "pen_name", columnDefinition = "VARCHAR(255)", nullable = false)
    private String penName;

    @Column(name = "nationality", columnDefinition = "VARCHAR(255)", nullable = false)
    private String nationality;

    @Convert(converter = MyLocalDateConverter.class)
    @Column(name = "born_date", nullable = false, columnDefinition = "DATE")
    private MyLocalDate born;

    @Convert(converter = MyLocalDateConverter.class)
    @Column(name = "died_date", columnDefinition = "DATE")
    private MyLocalDate died;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "admin_username",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_author_admin")
    )
    private Admin admin;

    public Author(
            String name,
            String penName,
            String nationality,
            MyLocalDate born,
            MyLocalDate died,
            Admin admin
    ) {
        this.name = name;
        this.penName = penName;
        this.nationality = nationality;
        this.born = born;
        this.died = died;
        this.admin = admin;
    }
}
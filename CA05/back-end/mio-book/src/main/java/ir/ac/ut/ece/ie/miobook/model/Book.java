package ir.ac.ut.ece.ie.miobook.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Book {
    String title;
    Author author;
    String publisher;
    int year;
    List<String> genres;
    int price;
    String synopsis;
    String content;

    public Book(String title, Author author, String publisher, int year, List<String> genres, int price, String synopsis, String content) {
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.year = year;
        this.genres = genres;
        this.price = price;
        this.synopsis = synopsis;
        this.content = content;
    }
}
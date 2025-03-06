package ir.ac.ut.ece.ie.miobook.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class Book {
    String title;
    Author author;
    String publisher;
    int year;
    List<String> genres;
    int price;
    String synopsis;
    String content;

    public Book(
            @JsonProperty("title") String title,
            @JsonProperty("author") Author author,
            @JsonProperty("publisher") String publisher,
            @JsonProperty("year") int year,
            @JsonProperty("genres") List<String> genres,
            @JsonProperty("price") Object price,
            @JsonProperty("synopsis") String synopsis,
            @JsonProperty("content") String content) {
        this.title = title;
        this.author = author;
        this.publisher = publisher;

        if (year <= 0) {
            throw new IllegalArgumentException("Year must be a positive number.");
        }
        this.year = year;

        if (genres == null || genres.isEmpty()) {
            throw new IllegalArgumentException("Book must have at least one genre.");
        }
        this.genres = genres;

        if (price instanceof Double) {
            this.price = (int) Math.round((Double) price * 100);
        } else if (price instanceof Integer) {
            this.price = (Integer) price;
        } else {
            throw new IllegalArgumentException("Price must be a number.");
        }

        this.synopsis = synopsis;
        this.content = content;
    }
}
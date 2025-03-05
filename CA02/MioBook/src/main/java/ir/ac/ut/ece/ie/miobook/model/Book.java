package ir.ac.ut.ece.ie.miobook.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

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
            @JsonProperty("price") double price,
            @JsonProperty("synopsis") String synopsis,
            @JsonProperty("content") String content) {
        this.title = title;
        this.author = author;
        this.publisher = publisher;

        if (year > 0) {
            this.year = year;
        } else {
            throw new IllegalArgumentException("Year must be a natural number.");
        }

        if (genres == null || genres.isEmpty()) {
            throw new IllegalArgumentException("Book must have at least one genre.");
        } else {
            this.genres = genres;
        }

        this.price = (int) Math.round(price * 100);
        this.synopsis = synopsis;
        this.content = content;
    }

    public Book(
            @JsonProperty("title") String title,
            @JsonProperty("author") Author author,
            @JsonProperty("publisher") String publisher,
            @JsonProperty("year") int year,
            @JsonProperty("genres") List<String> genres,
            @JsonProperty("price") int price,
            @JsonProperty("synopsis") String synopsis,
            @JsonProperty("content") String content) {
        this.title = title;
        this.author = author;
        this.publisher = publisher;

        if (year > 0) {
            this.year = year;
        } else {
            throw new IllegalArgumentException("Year must be a natural number.");
        }

        if (genres == null || genres.isEmpty()) {
            throw new IllegalArgumentException("Book must have at least one genre.");
        } else {
            this.genres = genres;
        }

        this.price = price;
        this.synopsis = synopsis;
        this.content = content;
    }
}

package ir.ac.ut.ece.ie.miobook.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class Review {
    int rate;
    String comment;
    LocalDate date;
    String title;
    String username;


    public Review(
            @JsonProperty("username") String username,
            @JsonProperty("title") String title,
            @JsonProperty("rate") int rate,
            @JsonProperty("comment") String comment)
    {
        if (title == null || username == null || comment == null){
            throw new IllegalArgumentException("Fill all fields...");
        }
        this.title = title;
        this.username = username;
        this.comment = comment;
        this.date = LocalDate.now();
        this.rate = rate;
    }
}

package ir.ac.ut.ece.ie.miobook.model;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Review {
    int rate;
    String comment;
    LocalDateTime date;
    String title;
    String username;

    public Review(String username, String title, int rate, String comment, LocalDateTime date)
    {
        this.title = title;
        this.username = username;
        this.comment = comment;
        this.date = date;
        this.rate = rate;
    }
}

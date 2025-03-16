package ir.ac.ut.ece.ie.miobook.model;

import lombok.Getter;
import java.time.LocalDate;

@Getter
public class Review {
    int rate;
    String comment;
    LocalDate date;
    String title;
    String username;

    public Review(String username, String title, int rate, String comment, LocalDate date)
    {
        this.title = title;
        this.username = username;
        this.comment = comment;
        this.date = date;
        this.rate = rate;
    }
}

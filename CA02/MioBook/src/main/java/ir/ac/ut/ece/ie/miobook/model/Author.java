package ir.ac.ut.ece.ie.miobook.model;
import com.fasterxml.jackson.annotation.JsonProperty;
import ir.ac.ut.ece.ie.miobook.validator.BornDiedValidator;

import java.time.LocalDate;

public class Author {

    String name;
    String penName;
    String nationality;
    LocalDate born;
    LocalDate died;
    boolean successful;

    public Author(@JsonProperty("name") String name,
                  @JsonProperty("penName") String penName, @JsonProperty("nationality") String nationality,
                  @JsonProperty("born") String born, @JsonProperty("died") String died) {
        this.name = name;
        this.penName = penName;
        this.nationality = nationality;
        try {
            this.born = LocalDate.parse(born);
        }catch (Exception e) {
            successful = false;
        }
        if (died == null || died.isEmpty()) {
            this.died = null;
        } else {
            try {
                this.died = LocalDate.parse(died);
            }catch (Exception e) {
                successful = false;
            }
        }
        if (!BornDiedValidator.validateBornDied(this.born, this.died)) {
            throw new IllegalArgumentException("Invalid dates!");
        }

    }
}
package ir.ac.ut.ece.ie.miobook.model;
import com.fasterxml.jackson.annotation.JsonProperty;
import ir.ac.ut.ece.ie.miobook.validator.BornDiedValidator;
import java.time.format.DateTimeParseException;

import java.time.LocalDate;

public class Author {

    String name;
    String penName;
    String nationality;
    LocalDate born;
    LocalDate died;

    public Author(@JsonProperty("name") String name,
                  @JsonProperty("penName") String penName, @JsonProperty("nationality") String nationality,
                  @JsonProperty("born") LocalDate born, @JsonProperty("died") LocalDate died) {
        this.name = name;
        this.penName = penName;
        this.nationality = nationality;
        this.born = born;
        this.died = died;

        if (!BornDiedValidator.validateBornDied(this.born, this.died)) {
            throw new IllegalArgumentException("Invalid dates!");
        }

    }

    public String GetName(){
        return this.name;
    }
}
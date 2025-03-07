package ir.ac.ut.ece.ie.miobook.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import ir.ac.ut.ece.ie.miobook.validator.BornDiedValidator;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
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
}
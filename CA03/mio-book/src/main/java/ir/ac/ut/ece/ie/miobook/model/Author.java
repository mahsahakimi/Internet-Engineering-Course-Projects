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
    MySimpleDate born;
    MySimpleDate died;

    public Author(String name, String penName, String nationality, MySimpleDate born, MySimpleDate died) {
        this.name = name;
        this.penName = penName;
        this.nationality = nationality;
        this.born = born;
        this.died = died;
    }
}
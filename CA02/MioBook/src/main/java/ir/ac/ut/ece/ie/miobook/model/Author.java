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
                  @JsonProperty("born") String born, @JsonProperty("died") String died) {
        this.name = name;
        this.penName = penName;
        this.nationality = nationality;
        this.born = StrToLocalDate(born, "born");
        if(died == null){
            this.died = null;
        }
        else{
            this.died = StrToLocalDate(died, "died");
        }
        if (!BornDiedValidator.validateBornDied(this.born, this.died)) {
            throw new IllegalArgumentException("Invalid dates!");
        }

    }
    private LocalDate StrToLocalDate(String dateStr,  String name) {
        if (dateStr == null || dateStr.isEmpty()) {
            throw new java.time.DateTimeException( name + " date is not valid...");
        }
        try {
            return LocalDate.parse(dateStr);
        } catch (DateTimeParseException  e) {
            throw new DateTimeParseException(name + " date is not valid...", "invalid-date", 0);
        }
    }
}
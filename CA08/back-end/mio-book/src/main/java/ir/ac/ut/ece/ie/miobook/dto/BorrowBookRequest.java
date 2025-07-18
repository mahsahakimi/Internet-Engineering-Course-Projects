package ir.ac.ut.ece.ie.miobook.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ir.ac.ut.ece.ie.miobook.exceptions.MissingRequiredFieldsException;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BorrowBookRequest {
    private String title;
    private Integer days;

    public void validate() {
        if (title == null || title.isEmpty()) {
            throw new MissingRequiredFieldsException("title");
        }
        if (days == null) {
            throw new MissingRequiredFieldsException("days");
        }
    }
}
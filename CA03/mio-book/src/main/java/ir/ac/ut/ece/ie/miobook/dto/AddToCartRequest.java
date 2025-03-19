package ir.ac.ut.ece.ie.miobook.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ir.ac.ut.ece.ie.miobook.exceptions.MissingRequiredFieldsException;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddToCartRequest {
    private String username;
    private String title;

    public void validate() {
        if (username == null || username.isEmpty()) {
            throw new MissingRequiredFieldsException("username");
        }
        if (title == null || title.isEmpty()) {
            throw new MissingRequiredFieldsException("title");
        }
    }
}
package ir.ac.ut.ece.ie.miobook.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ir.ac.ut.ece.ie.miobook.exceptions.MissingRequiredFieldsException;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddCreditRequest {
    private String username;
    private Integer credit;

    public void validate() {
        if (username == null || username.isEmpty()) {
            throw new MissingRequiredFieldsException("username");
        }
        if (credit == null) {
            throw new MissingRequiredFieldsException("credit");
        }
    }
}
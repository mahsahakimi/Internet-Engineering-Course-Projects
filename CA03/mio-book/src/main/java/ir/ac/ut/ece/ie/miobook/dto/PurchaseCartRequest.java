package ir.ac.ut.ece.ie.miobook.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ir.ac.ut.ece.ie.miobook.exceptions.MissingRequiredFieldsException;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PurchaseCartRequest {
    private String username;

    public void validate() {
        if (username == null || username.isEmpty()) {
            throw new MissingRequiredFieldsException("username");
        }
    }
}
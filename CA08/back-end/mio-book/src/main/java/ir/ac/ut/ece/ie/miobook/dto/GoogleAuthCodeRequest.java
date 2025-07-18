package ir.ac.ut.ece.ie.miobook.dto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ir.ac.ut.ece.ie.miobook.exceptions.MissingRequiredFieldsException;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoogleAuthCodeRequest {
    private String code;

    public void validate() {
        if (code == null || code.isEmpty()) {
            throw new MissingRequiredFieldsException("code");
        }
    }
}
package ir.ac.ut.ece.ie.miobook.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ir.ac.ut.ece.ie.miobook.exceptions.MissingRequiredFieldsException;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddReviewRequest {
    private String username;
    private String title;
    private Integer rate;
    private String comment;

    public void validate() {
        if (username == null || username.isEmpty()) {
            throw new MissingRequiredFieldsException("username");
        }
        if (title == null || title.isEmpty()) {
            throw new MissingRequiredFieldsException("title");
        }
        if (rate == null) {
            throw new MissingRequiredFieldsException("rate");
        }
        if (comment == null || comment.isEmpty()) {
            throw new MissingRequiredFieldsException("comment");
        }
    }
}
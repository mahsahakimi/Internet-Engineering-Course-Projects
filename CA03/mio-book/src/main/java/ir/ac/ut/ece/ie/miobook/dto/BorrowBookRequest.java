package ir.ac.ut.ece.ie.miobook.dto;

import ir.ac.ut.ece.ie.miobook.exceptions.MissingRequiredFieldsException;
import lombok.Data;

@Data
public class BorrowBookRequest {
    private String username;
    private String title;
    private Integer days;

    public void validate() {
        if (username == null || username.isEmpty()) {
            throw new MissingRequiredFieldsException("username");
        }
        if (title == null || title.isEmpty()) {
            throw new MissingRequiredFieldsException("title");
        }
        if (days == null) {
            throw new MissingRequiredFieldsException("days");
        }
    }
}
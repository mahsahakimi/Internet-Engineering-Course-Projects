package ir.ac.ut.ece.ie.miobook.dto;

import ir.ac.ut.ece.ie.miobook.exceptions.MissingRequiredFieldsException;
import lombok.Data;

@Data
public class AddAuthorRequest {
    private String username;
    private String name;
    private String penName;
    private String nationality;
    private String born;
    private String died;

    public void validate() {
        if (username == null || username.isEmpty()) {
            throw new MissingRequiredFieldsException("username");
        }
        if (name == null || name.isEmpty()) {
            throw new MissingRequiredFieldsException("name");
        }
        if (penName == null || penName.isEmpty()) {
            throw new MissingRequiredFieldsException("penName");
        }
        if (nationality == null || nationality.isEmpty()) {
            throw new MissingRequiredFieldsException("nationality");
        }
        if (born == null || born.isEmpty()) {
            throw new MissingRequiredFieldsException("born");
        }
    }
}
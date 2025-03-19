package ir.ac.ut.ece.ie.miobook.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ir.ac.ut.ece.ie.miobook.exceptions.MissingRequiredFieldsException;
import ir.ac.ut.ece.ie.miobook.model.Address;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddUserRequest {
    private String role;
    private String username;
    private String password;
    private String email;
    private Address address;

    // Validation method
    public void validate() {
        if (role == null || role.isEmpty()) {
            throw new MissingRequiredFieldsException("role");
        }
        if (username == null || username.isEmpty()) {
            throw new MissingRequiredFieldsException("username");
        }
        if (password == null || password.isEmpty()) {
            throw new MissingRequiredFieldsException("password");
        }
        if (email == null || email.isEmpty()) {
            throw new MissingRequiredFieldsException("email");
        }
        if (address == null) {
            throw new MissingRequiredFieldsException("address");
        }
        if (address.getCountry() == null || address.getCountry().isEmpty()) {
            throw new MissingRequiredFieldsException("address: country");
        }
        if (address.getCity() == null || address.getCity().isEmpty()) {
            throw new MissingRequiredFieldsException("address: city");
        }
    }
}
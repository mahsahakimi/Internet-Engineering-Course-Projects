package ir.ac.ut.ece.ie.miobook.validator;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PasswordValidatorTest {

    @Test
    public void testValidPassword() {
        assertTrue(PasswordValidator.isValid("1234"));
        assertTrue(PasswordValidator.isValid("password"));
        assertTrue(PasswordValidator.isValid("strongPassword123"));
    }

    @Test
    public void testInvalidPasswordTooShort() {
        assertFalse(PasswordValidator.isValid("123"));
        assertFalse(PasswordValidator.isValid("ab"));
        assertFalse(PasswordValidator.isValid(""));
    }

    @Test
    public void testInvalidPasswordNull() {
        assertFalse(PasswordValidator.isValid(null));
    }
}
package ir.ac.ut.ece.ie.miobook.validator;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UsernameValidatorTest {

    @Test
    public void testValidUsername() {
        assertTrue(UsernameValidator.isValid("Cool_User-123"));
        assertTrue(UsernameValidator.isValid("anotherUser"));
        assertTrue(UsernameValidator.isValid("user123"));
        assertTrue(UsernameValidator.isValid("User_Name"));
        assertTrue(UsernameValidator.isValid("user-name"));
    }

    @Test
    public void testInvalidUsernameEmpty() {
        assertFalse(UsernameValidator.isValid(""));
    }

    @Test
    public void testInvalidUsernameNull() {
        assertFalse(UsernameValidator.isValid(null));
    }

    @Test
    public void testInvalidUsernameWithSpace() {
        assertFalse(UsernameValidator.isValid("Cool User"));
    }

    @Test
    public void testInvalidUsernameWithSpecialChars() {
        assertFalse(UsernameValidator.isValid("!CoolUser"));
        assertFalse(UsernameValidator.isValid("User@123"));
        assertFalse(UsernameValidator.isValid("User#"));
    }
}

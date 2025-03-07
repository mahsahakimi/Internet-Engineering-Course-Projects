package ir.ac.ut.ece.ie.miobook.validator;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EmailValidatorTest {

    @Test
    public void testValidEmail() {
        assertTrue(EmailValidator.isValid("test@example.com"));
        assertTrue(EmailValidator.isValid("john.doe123@sub.domain.co"));
        assertTrue(EmailValidator.isValid("test.name@sub.domain.example.com"));
        assertTrue(EmailValidator.isValid("user-name@domain.com"));
        assertTrue(EmailValidator.isValid("user.name@domain.com"));
        assertTrue(EmailValidator.isValid("user_name@domain.com"));
        assertTrue(EmailValidator.isValid("test@domain-name.com"));
        assertTrue(EmailValidator.isValid("test@domain.name.com"));
    }

    @Test
    public void testInvalidEmail() {
        assertFalse(EmailValidator.isValid("testexample.com"));
        assertFalse(EmailValidator.isValid("test@example"));
        assertFalse(EmailValidator.isValid("test @example.com"));
        assertFalse(EmailValidator.isValid(""));
        assertFalse(EmailValidator.isValid(null));
        assertFalse(EmailValidator.isValid("@."));
        assertFalse(EmailValidator.isValid("test@.com."));
        assertFalse(EmailValidator.isValid("test@."));
        assertFalse(EmailValidator.isValid("test@example..com"));
        assertFalse(EmailValidator.isValid("test..name@example.com"));
        assertFalse(EmailValidator.isValid("test!name@example.com"));
        assertFalse(EmailValidator.isValid("test@example!.com"));
    }

    @Test
    public void testLongTLD() {
        assertTrue(EmailValidator.isValid("test@example.museum"));
    }

    @Test
    public void testDomainWithHyphen() {
        assertTrue(EmailValidator.isValid("test@sub-domain.com"));
    }

    @Test
    public void testEmailWithNumberInTLD() {
        assertTrue(EmailValidator.isValid("test@example.co1"));
    }
}
package ir.ac.ut.ece.ie.miobook.validator;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AddressValidatorTest {

    @Test
    public void testValidAddress() {
        assertTrue(AddressValidator.isValid("USA", "New York"));
    }

    @Test
    public void testInvalidAddressEmptyCountry() {
        assertFalse(AddressValidator.isValid("", "New York"));
    }

    @Test
    public void testInvalidAddressEmptyCity() {
        assertFalse(AddressValidator.isValid("USA", ""));
    }

    @Test
    public void testInvalidAddressNullCountry() {
        assertFalse(AddressValidator.isValid(null, "New York"));
    }

    @Test
    public void testInvalidAddressNullCity() {
        assertFalse(AddressValidator.isValid("USA", null));
    }

    @Test
    public void testInvalidAddressSpacesOnlyCountry() {
        assertFalse(AddressValidator.isValid("   ", "New York"));
    }

    @Test
    public void testInvalidAddressSpacesOnlyCity() {
        assertFalse(AddressValidator.isValid("USA", "   "));
    }
}


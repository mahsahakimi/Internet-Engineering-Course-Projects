package ir.ac.ut.ece.ie.miobook.validator;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class BornDiedValidatorTest {

    @Test
    void testValidBornBeforeDied() {
        LocalDate born = LocalDate.of(1950, 5, 20);
        LocalDate died = LocalDate.of(2000, 10, 15);
        assertTrue(BornDiedValidator.validateBornDied(born, died));
    }

    @Test
    void testBornDateIsNull() {
        LocalDate died = LocalDate.of(2000, 10, 15);
        assertFalse(BornDiedValidator.validateBornDied(null, died));
    }

    @Test
    void testDiedDateIsNull() {
        LocalDate born = LocalDate.of(1950, 5, 20);
        assertTrue(BornDiedValidator.validateBornDied(born, null));
    }

    @Test
    void testBornEqualsDied() {
        LocalDate date = LocalDate.of(1990, 1, 1);
        assertFalse(BornDiedValidator.validateBornDied(date, date));
    }

    @Test
    void testBornAfterDied() {
        LocalDate born = LocalDate.of(2020, 5, 20);
        LocalDate died = LocalDate.of(2000, 10, 15);
        assertFalse(BornDiedValidator.validateBornDied(born, died));
    }
}
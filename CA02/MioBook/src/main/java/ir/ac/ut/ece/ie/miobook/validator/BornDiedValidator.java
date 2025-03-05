package ir.ac.ut.ece.ie.miobook.validator;
import java.time.LocalDate;

public class BornDiedValidator {

    public static boolean validateBornDied(LocalDate bornDate, LocalDate diedDate) {
        if (bornDate == null) {
            System.out.println("Born date cannot be null!");
            return false;
        }

        if (diedDate != null && !bornDate.isBefore(diedDate)) {
            System.out.println("Born date must be before died date!");
            return false;
        }

        return true;
    }
}
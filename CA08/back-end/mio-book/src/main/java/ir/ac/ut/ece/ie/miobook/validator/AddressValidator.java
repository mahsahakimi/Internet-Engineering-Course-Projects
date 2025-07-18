package ir.ac.ut.ece.ie.miobook.validator;

public class AddressValidator {

    public static boolean isValid(String country, String city) {
        if (country == null || country.trim().isEmpty()) {
            return false;
        }
        if (city == null || city.trim().isEmpty()) {
            return false;
        }

        return true;
    }
}

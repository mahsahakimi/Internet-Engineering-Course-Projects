package ir.ac.ut.ece.ie.miobook.exceptions;

public class SortByException extends IllegalArgumentException {

    private static final String ERROR_CODE = "INVALID_SORT_BY";

    public SortByException(String sortBy) {
        super(String.format("Error [%s]: Invalid sortBy value: %s. Allowed values: averageRating, reviewCount.", ERROR_CODE, sortBy));
    }

    public String getErrorCode() {
        return ERROR_CODE;
    }
}
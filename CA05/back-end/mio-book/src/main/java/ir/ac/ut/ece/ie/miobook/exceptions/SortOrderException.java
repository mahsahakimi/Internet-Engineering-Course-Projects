package ir.ac.ut.ece.ie.miobook.exceptions;

public class SortOrderException extends IllegalArgumentException {

    private static final String ERROR_CODE = "INVALID_SORT_ORDER";

    public SortOrderException(String sortOrder) {
        super(String.format("Error [%s]: Invalid sort order: %s. Allowed values are: asc, desc.", ERROR_CODE, sortOrder));
    }

    public String getErrorCode() {
        return ERROR_CODE;
    }
}
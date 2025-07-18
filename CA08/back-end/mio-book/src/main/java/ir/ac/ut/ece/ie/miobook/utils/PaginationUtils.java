package ir.ac.ut.ece.ie.miobook.utils;

import java.util.List;

public class PaginationUtils {

    public static <T> List<T> paginate(List<T> items, int page, int pageSize) {
        if (items == null || items.isEmpty()) {
            return List.of();
        }

        int startIndex = (page - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, items.size());

        if (startIndex >= items.size() || startIndex < 0) {
            return List.of();
        }

        return items.subList(startIndex, endIndex);
    }
}
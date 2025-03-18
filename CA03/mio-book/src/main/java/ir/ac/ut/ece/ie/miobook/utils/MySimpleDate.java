package ir.ac.ut.ece.ie.miobook.utils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import ir.ac.ut.ece.ie.miobook.exceptions.InvalidDateFormatException;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Getter
@Component
public class MySimpleDate {
    private int year;
    private int month;
    private int day;

    @JsonCreator
    public MySimpleDate(
            @JsonProperty("year") int year,
            @JsonProperty("month") int month,
            @JsonProperty("day") int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public MySimpleDate() {
        this.year = 0;
        this.month = 0;
        this.day = 0;
    }

    public static MySimpleDate parse(String dateString) {
        // the dateString to be in "yyyy-MM-dd" format
        String[] parts = dateString.split("-");

        if (parts.length != 3) {
            throw new InvalidDateFormatException();
        }

        try {
            int year = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);
            int day = Integer.parseInt(parts[2]);
            return new MySimpleDate(year, month, day);
        } catch (NumberFormatException e) {
            throw new InvalidDateFormatException();
        }
    }

    @Override
    public String toString() {
        if (this.year == 0 && this.month == 0 && this.day == 0) {
            return null;
        }
        return String.format("%04d-%02d-%02d", year, month, day);
    }

    // Function to convert MySimpleDate to LocalDate
    public LocalDate toLocalDate() {
        if (this.year == 0 && this.month == 0 && this.day == 0) {
            return null;
        }
        return LocalDate.of(this.year, this.month, this.day);
    }

    // Function to create MySimpleDate from LocalDate
    public static MySimpleDate fromLocalDate(LocalDate localDate) {
        if (localDate == null) {
            return new MySimpleDate();
        }
        return new MySimpleDate(localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth());
    }

    public static MySimpleDate of(int year, int month, int day) {
        if (year == 0 && month == 0 && day == 0) {
            return null;
        }
        return new MySimpleDate(year, month, day);
    }
}

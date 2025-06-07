package ir.ac.ut.ece.ie.miobook.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import ir.ac.ut.ece.ie.miobook.exceptions.InvalidDateFormatException;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class MyLocalDate {
    private int year;
    private int month;
    private int day;

    @JsonCreator
    public MyLocalDate(
            @JsonProperty("year") int year,
            @JsonProperty("month") int month,
            @JsonProperty("day") int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public MyLocalDate() {
        this.year = 0;
        this.month = 0;
        this.day = 0;
    }

    // constructor to create MySimpleDate from a String
    public MyLocalDate(String dateString) {
        // the dateString to be in "yyyy-MM-dd" format
        String[] parts = dateString.split("-");

        if (parts.length != 3) {
            throw new InvalidDateFormatException();
        }

        try {
            this.year = Integer.parseInt(parts[0]);
            this.month = Integer.parseInt(parts[1]);
            this.day = Integer.parseInt(parts[2]);
        } catch (NumberFormatException e) {
            throw new InvalidDateFormatException();
        }
    }

    public static MyLocalDate parse(String dateString) {
        // the dateString to be in "yyyy-MM-dd" format
        String[] parts = dateString.split("-");

        if (parts.length != 3) {
            throw new InvalidDateFormatException();
        }

        try {
            int year = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);
            int day = Integer.parseInt(parts[2]);
            return new MyLocalDate(year, month, day);
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
    public static MyLocalDate fromLocalDate(LocalDate localDate) {
        if (localDate == null) {
            return new MyLocalDate();
        }
        return new MyLocalDate(localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth());
    }

    public static MyLocalDate of(int year, int month, int day) {
        if (year == 0 && month == 0 && day == 0) {
            return null;
        }
        return new MyLocalDate(year, month, day);
    }
}

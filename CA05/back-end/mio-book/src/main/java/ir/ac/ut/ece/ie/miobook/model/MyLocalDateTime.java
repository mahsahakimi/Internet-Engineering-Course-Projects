package ir.ac.ut.ece.ie.miobook.model;

import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class MyLocalDateTime {
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private int second;

    public MyLocalDateTime(int year, int month, int day, int hour, int minute, int second) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.second = second;
    }

    public MyLocalDateTime(LocalDateTime localDateTime) {
        this.year = localDateTime.getYear();
        this.month = localDateTime.getMonthValue();
        this.day = localDateTime.getDayOfMonth();
        this.hour = localDateTime.getHour();
        this.minute = localDateTime.getMinute();
        this.second = localDateTime.getSecond();
    }

    public MyLocalDateTime(String dateTimeString) {
        // the dateTimeString to be in "yyyy-MM-dd HH:mm:ss" format
        String[] parts = dateTimeString.split(" ");
        String[] dateParts = parts[0].split("-");
        String[] timeParts = parts[1].split(":");

        int year = Integer.parseInt(dateParts[0]);
        int month = Integer.parseInt(dateParts[1]);
        int day = Integer.parseInt(dateParts[2]);
        int hour = Integer.parseInt(timeParts[0]);
        int minute = Integer.parseInt(timeParts[1]);
        int second = Integer.parseInt(timeParts[2]);

        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.second = second;
    }

    @Override
    public String toString() {
        return String.format("%04d-%02d-%02d %02d:%02d:%02d", year, month, day, hour, minute, second);
    }

    public static MyLocalDateTime parse(String dateTimeString) {
        // the dateTimeString to be in "yyyy-MM-dd HH:mm:ss" format
        String[] parts = dateTimeString.split(" ");
        String[] dateParts = parts[0].split("-");
        String[] timeParts = parts[1].split(":");

        int year = Integer.parseInt(dateParts[0]);
        int month = Integer.parseInt(dateParts[1]);
        int day = Integer.parseInt(dateParts[2]);
        int hour = Integer.parseInt(timeParts[0]);
        int minute = Integer.parseInt(timeParts[1]);
        int second = Integer.parseInt(timeParts[2]);

        return new MyLocalDateTime(year, month, day, hour, minute, second);
    }

    public static MyLocalDateTime now() {
        LocalDateTime now = LocalDateTime.now();
        return new MyLocalDateTime(now.getYear(), now.getMonthValue(), now.getDayOfMonth(),
                now.getHour(), now.getMinute(), now.getSecond());
    }

    public boolean isBefore(MyLocalDateTime other) {
        LocalDateTime thisDateTime = LocalDateTime.of(this.year, this.month, this.day, this.hour, this.minute, this.second);
        LocalDateTime otherDateTime = LocalDateTime.of(other.year, other.month, other.day, other.hour, other.minute, other.second);

        return thisDateTime.isBefore(otherDateTime);
    }

    public static long daysBetween(MyLocalDateTime startDate, MyLocalDateTime endDate) {
        LocalDateTime startDateTime = LocalDateTime.of(
                startDate.getYear(), startDate.getMonth(), startDate.getDay(),
                startDate.getHour(), startDate.getMinute(), startDate.getSecond()
        );
        LocalDateTime endDateTime = LocalDateTime.of(
                endDate.getYear(), endDate.getMonth(), endDate.getDay(),
                endDate.getHour(), endDate.getMinute(), endDate.getSecond()
        );

        return java.time.temporal.ChronoUnit.DAYS.between(startDateTime.toLocalDate(), endDateTime.toLocalDate());
    }

    public MyLocalDateTime plusDays(long daysToAdd) {
        LocalDate localDate = LocalDate.of(this.year, this.month, this.day);

        LocalDate newLocalDate = localDate.plusDays(daysToAdd);

        return new MyLocalDateTime(newLocalDate.getYear(), newLocalDate.getMonthValue(),
                newLocalDate.getDayOfMonth(), this.hour, this.minute, this.second);
    }
}
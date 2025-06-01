package ir.ac.ut.ece.ie.miobook.converter;

import ir.ac.ut.ece.ie.miobook.model.MyLocalDateTime;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.time.LocalDateTime;

@Converter(autoApply = true)
public class MyLocalDateTimeConverter implements AttributeConverter<MyLocalDateTime, LocalDateTime> {

    @Override
    public LocalDateTime convertToDatabaseColumn(MyLocalDateTime myLocalDateTime) {
        if (myLocalDateTime == null) return null;
        return LocalDateTime.of(
                myLocalDateTime.getYear(),
                myLocalDateTime.getMonth(),
                myLocalDateTime.getDay(),
                myLocalDateTime.getHour(),
                myLocalDateTime.getMinute(),
                myLocalDateTime.getSecond()
        );
    }

    @Override
    public MyLocalDateTime convertToEntityAttribute(LocalDateTime localDateTime) {
        return (localDateTime == null) ? null : new MyLocalDateTime(localDateTime);
    }
}
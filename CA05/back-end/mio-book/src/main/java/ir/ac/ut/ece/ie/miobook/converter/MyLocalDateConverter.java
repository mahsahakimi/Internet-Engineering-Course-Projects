package ir.ac.ut.ece.ie.miobook.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.time.LocalDate;

import ir.ac.ut.ece.ie.miobook.model.MyLocalDate;

@Converter(autoApply = true)
public class MyLocalDateConverter implements AttributeConverter<MyLocalDate, LocalDate> {

    @Override
    public LocalDate convertToDatabaseColumn(MyLocalDate myLocalDate) {
        return (myLocalDate == null) ? null : myLocalDate.toLocalDate();
    }

    @Override
    public MyLocalDate convertToEntityAttribute(LocalDate localDate) {
        return (localDate == null) ? null : MyLocalDate.fromLocalDate(localDate);
    }
}

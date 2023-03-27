package es.tatanca.logistics.entities.Driver;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.stream.Stream;

@Converter(autoApply = true)
public class DriverStatusConverter implements AttributeConverter<DriverStatus, String> {

    @Override
    public String convertToDatabaseColumn(DriverStatus driverStatus) {
        if (driverStatus == null) {
            return null;
        }
        return driverStatus.getCode();
    }

    @Override
    public DriverStatus convertToEntityAttribute(String code) {
        if (code == null) {
            return null;
        }

        return Stream.of(DriverStatus.values())
                .filter(c -> c.getCode().equals(code))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

}
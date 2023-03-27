package es.tatanca.logistics.entities.Cargo;

import es.tatanca.logistics.entities.Truck.TruckStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.stream.Stream;

@Converter(autoApply = true)
public class CargoStatusConverter implements AttributeConverter<TruckStatus, String> {

    @Override
    public String convertToDatabaseColumn(TruckStatus truckStatus) {
        if (truckStatus == null) {
            return null;
        }
        return truckStatus.getCode();
    }

    @Override
    public TruckStatus convertToEntityAttribute(String code) {
        if (code == null) {
            return null;
        }

        return Stream.of(TruckStatus.values())
                .filter(c -> c.getCode().equals(code))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

}
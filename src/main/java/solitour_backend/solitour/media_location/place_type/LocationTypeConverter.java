package solitour_backend.solitour.media_location.place_type;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class LocationTypeConverter implements AttributeConverter<LocatoinType, String> {

    @Override
    public String convertToDatabaseColumn(LocatoinType locatoinType) {
        return locatoinType.getType();
    }

    @Override
    public LocatoinType convertToEntityAttribute(String dbData) {
        return LocatoinType.fromName(dbData);
    }
}

package solitour_backend.solitour.tourist_spot.spot_type;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import solitour_backend.solitour.media_location.place_type.LocatoinType;

@Converter(autoApply = true)
public class SpotTypeConverter implements AttributeConverter<SpotType, String> {

    @Override
    public String convertToDatabaseColumn(SpotType spotType) {
        return spotType.getType();
    }

    @Override
    public SpotType convertToEntityAttribute(String dbData) {
        return SpotType.fromName(dbData);
    }
}

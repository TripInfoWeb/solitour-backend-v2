package solitour_backend.solitour.media_location.media_type;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class MediaTypeConverter implements AttributeConverter<MediaType, String> {

    @Override
    public String convertToDatabaseColumn(MediaType mediaType) {
        if (mediaType == null) {
            return null;
        }
        return mediaType.getType();
    }

    @Override
    public MediaType convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return MediaType.fromName(dbData);
    }
}

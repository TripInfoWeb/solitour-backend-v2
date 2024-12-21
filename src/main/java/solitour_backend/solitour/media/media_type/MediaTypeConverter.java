package solitour_backend.solitour.media.media_type;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class MediaTypeConverter implements AttributeConverter<MediaType, String> {

    @Override
    public String convertToDatabaseColumn(MediaType mediaType) {
        return mediaType.getType();
    }

    @Override
    public MediaType convertToEntityAttribute(String dbData) {
        return MediaType.fromName(dbData);
    }
}

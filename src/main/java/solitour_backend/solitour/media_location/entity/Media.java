package solitour_backend.solitour.media_location.entity;

import jakarta.persistence.*;
import lombok.Getter;
import solitour_backend.solitour.media_location.media_type.MediaType;
import solitour_backend.solitour.media_location.media_type.MediaTypeConverter;

@Entity
@Getter
@Table (name = "media")
public class Media {
    @Id
    @Column(name = "media_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "media_type")
    @Convert(converter = MediaTypeConverter.class)
    private MediaType mediaType;

    @Column(name = "media_name")
    private String mediaName;

    @Column(name = "media_image")
    private String mediaImage;

}

package solitour_backend.solitour.media_location.entity;

import com.amazonaws.services.ec2.model.LocationType;
import jakarta.persistence.*;
import lombok.Getter;
import solitour_backend.solitour.media_location.media_type.MediaType;
import solitour_backend.solitour.media_location.media_type.MediaTypeConverter;
import solitour_backend.solitour.media_location.place_type.LocationTypeConverter;
import solitour_backend.solitour.travel_plan.service.Spot;

@Entity
@Getter
@Table(name = "media_location")
public class MediaLocation implements Spot {
    @Id
    @Column(name = "media_location_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "media_type")
    @Convert(converter = MediaTypeConverter.class)
    private MediaType mediaType;

    @Column(name = "media_name")
    private String mediaName;

    @Column(name = "place_name")
    private String placeName;

    @Column(name = "region", nullable = false)
    private String region;

    @Column(name = "location_type")
    @Convert(converter = LocationTypeConverter.class)
    private LocationType locationType;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "operating_hours")
    private String operatingHours;

    @Column(name = "address")
    private String address;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;
}

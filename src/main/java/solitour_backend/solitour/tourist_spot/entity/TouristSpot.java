package solitour_backend.solitour.tourist_spot.entity;

import jakarta.persistence.*;
import lombok.Getter;
import solitour_backend.solitour.media_location.place_type.LocationTypeConverter;
import solitour_backend.solitour.tourist_spot.spot_type.SpotType;
import solitour_backend.solitour.tourist_spot.spot_type.SpotTypeConverter;
import solitour_backend.solitour.travel_plan.service.Spot;

@Entity
@Getter
@Table(name = "tourist_spot")
public class TouristSpot implements Spot {
    @Id
    @Column(name = "tourist_spot_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

//    @Column(name = "region", nullable = false)
//    private String region;

    @Column(name = "spot_type")
    @Convert(converter = SpotTypeConverter.class)
    private SpotType spotType;

    @Column(name = "address")
    private String address;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;
}

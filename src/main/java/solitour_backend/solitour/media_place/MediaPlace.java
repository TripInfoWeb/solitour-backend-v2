package solitour_backend.solitour.media_place;

import jakarta.persistence.*;

@Entity
@Table(name = "media_place")
public class MediaPlace {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "media_type", nullable = false)
    private String mediaType;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "place_name", nullable = false)
    private String placeName;

    @Column(name = "place_type", nullable = false)
    private String placeType;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "hours")
    private String hours;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "latitude", nullable = false)
    private Double latitude;

    @Column(name = "longitude", nullable = false)
    private Double longitude;
}

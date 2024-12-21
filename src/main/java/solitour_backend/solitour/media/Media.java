package solitour_backend.solitour.media;

import jakarta.persistence.*;

@Entity
@Table(name = "media")
public class Media {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "media_id", nullable = false)
    private Media media;

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

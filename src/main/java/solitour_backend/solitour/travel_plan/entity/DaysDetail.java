package solitour_backend.solitour.travel_plan.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "days_detail")
public class DaysDetail {

    @Id
    @Column(name = "days_detail_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "place_name")
    private String placeName;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "days_id")
    private Days days;

    @Builder
    public DaysDetail(String placeName, Double latitude, Double longitude, Days day) {
        this.placeName = placeName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.days = day;
    }

    public void updateDetails(DaysDetail updatedDetails) {
        this.placeName = updatedDetails.placeName;
        this.latitude = updatedDetails.latitude;
        this.longitude = updatedDetails.longitude;
    }
}
package solitour_backend.solitour.travel_plan.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "days")
public class Days {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "day_number")
    private int dayNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id")
    private Plan plan;

    @Column(name = "locations", columnDefinition = "TEXT")
    private String locations;

    @Column(name = "restaurants", columnDefinition = "TEXT")
    private String restaurants;

}

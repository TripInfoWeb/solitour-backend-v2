package solitour_backend.solitour.travel_plan.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "plan")
public class Plan {
    @Id
    @Column(name = "plan_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content_category", nullable = false)
    private String contentCategory;

    @Column(name = "created_date", nullable = false)
    private String createdDate;

    @OneToMany(mappedBy = "plan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Days> days = new ArrayList<>();

    public void addDay(Days day) {
        this.days.add(day);

        day.setPlan(this);
    }
}


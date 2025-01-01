package solitour_backend.solitour.travel_plan.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import solitour_backend.solitour.user.entity.User;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "user_plan")
public class UserPlan {
    @Id
    @Column(name = "user_plan_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", nullable = false)
    private Plan plan;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @Builder
    public UserPlan(User user, Plan plan) {
        this.user = user;
        this.plan = plan;
        this.createdDate = LocalDateTime.now();
    }
}

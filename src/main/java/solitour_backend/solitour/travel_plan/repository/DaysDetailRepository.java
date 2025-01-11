package solitour_backend.solitour.travel_plan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import solitour_backend.solitour.travel_plan.entity.DaysDetail;
import solitour_backend.solitour.travel_plan.entity.UserPlan;

public interface DaysDetailRepository extends JpaRepository<DaysDetail, Long> {
}

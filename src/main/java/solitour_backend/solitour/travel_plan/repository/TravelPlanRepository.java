package solitour_backend.solitour.travel_plan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import solitour_backend.solitour.travel_plan.entity.Plan;

public interface TravelPlanRepository extends JpaRepository<Plan, Long> {
}

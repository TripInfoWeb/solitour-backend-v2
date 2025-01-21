package solitour_backend.solitour.travel_plan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import solitour_backend.solitour.travel_plan.entity.Days;
import solitour_backend.solitour.travel_plan.entity.Plan;

import java.util.List;

public interface DaysRepository extends JpaRepository<Days, Integer> {
    Days findByPlan(Plan plan);
}

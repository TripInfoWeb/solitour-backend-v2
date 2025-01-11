package solitour_backend.solitour.travel_plan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import solitour_backend.solitour.travel_plan.entity.UserPlan;

import java.util.List;

public interface UserPlanRepository extends JpaRepository<UserPlan, Long> {
    @Query("SELECT u FROM UserPlan u WHERE u.user.id = :userId")
    List<UserPlan> findByUserId(@Param("userId") Long userId);

    @Query("SELECT u FROM UserPlan u WHERE u.user.id = :userId")
    List<UserPlan> getUserPlanList(Long userId);

    @Query("SELECT u FROM UserPlan u WHERE u.id = :userPlanId")
    UserPlan getUserPlan(Long userPlanId);
}
package solitour_backend.solitour.travel_plan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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

    @Query("SELECT COUNT(u) > 0 FROM UserPlan u WHERE u.user.id = :userId AND u.plan.id = :planId")
    boolean checkUserPlan(Long userId, Long planId);

    @Modifying
    @Query("DELETE FROM UserPlan WHERE user.id = :userId AND plan.id = :planId")
    void deleteUserPlan(Long userId, Long planId);
}
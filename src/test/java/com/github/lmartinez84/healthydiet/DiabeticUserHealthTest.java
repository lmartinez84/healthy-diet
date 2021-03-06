package com.github.lmartinez84.healthydiet;

import com.github.lmartinez84.healthydiet.food.Food;
import com.github.lmartinez84.healthydiet.food.FoodGroup;
import com.github.lmartinez84.healthydiet.user.domain.Routine;
import com.github.lmartinez84.healthydiet.user.domain.User;
import com.github.lmartinez84.healthydiet.user.domain.dietary_requirement.DiabeticDietaryRequirement;
import com.github.lmartinez84.healthydiet.user.domain.dietary_requirement.DietaryRequirement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@Execution(ExecutionMode.CONCURRENT)
class DiabeticUserHealthTest {
    Set<DietaryRequirement> diabeticDietaryRequirement;

    @BeforeEach
    void setUp() {
        diabeticDietaryRequirement = Set.of(new DiabeticDietaryRequirement());
    }

    @Test
    void is_unhealthy_when_has_normal_weight_but_hasnt_an_active_routine_or_weight_under_or_equal_70kg() {
        User anUnhealthyDiabetic = UserObjectMother.aRandomUserWith()
                                                   .weight(91)
                                                   .height(1.8)
                                                   .dietaryRequirements(diabeticDietaryRequirement)
                                                   .favoriteFoods(Set.of(new Food("potatos",
                                                                                  FoodGroup.VEGETABLES_FRUITS_SEED,
                                                                                  Set.of())))
                                                   .routine(Routine.LOW)
                                                   .build();

        assertThat(anUnhealthyDiabetic.isHealthy()).isFalse();
    }

    @Test
    void is_healthy_when_has_an_active_routine() {
        User aHealthyDiabetic = UserObjectMother.aRandomUserWith()
                                                .weight(91)
                                                .height(1.8)
                                                .dietaryRequirements(diabeticDietaryRequirement)
                                                .routine(Routine.ACTIVE)
                                                .favoriteFoods(Set.of(new Food("potatos",
                                                                               FoodGroup.VEGETABLES_FRUITS_SEED,
                                                                               Set.of())))
                                                .build();
        assertThat(aHealthyDiabetic.isHealthy()).isTrue();
    }

    @Test
    void is_healthy_when_its_weight_is_less_than_or_equal_to_70() {
        User aHealthyDiabetic = UserObjectMother.aRandomUserWith()
                                                .weight(70)
                                                .height(1.8)
                                                .dietaryRequirements(diabeticDietaryRequirement)
                                                .routine(Routine.LOW)
                                                .favoriteFoods(Set.of(new Food("potatos",
                                                                               FoodGroup.VEGETABLES_FRUITS_SEED,
                                                                               Set.of())))
                                                .build();
        assertThat(aHealthyDiabetic.isHealthy()).isTrue();
    }
}

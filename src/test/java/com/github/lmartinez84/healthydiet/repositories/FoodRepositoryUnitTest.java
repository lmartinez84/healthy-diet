package com.github.lmartinez84.healthydiet.repositories;

import com.github.lmartinez84.healthydiet.food.Food;
import com.github.lmartinez84.healthydiet.food.FoodRepository;
import com.github.lmartinez84.healthydiet.food.adapters.InMemoryFoodRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import static org.assertj.core.api.Assertions.assertThat;

@Execution(ExecutionMode.CONCURRENT)
public abstract class FoodRepositoryUnitTest {
    protected FoodRepository foodRepository;

    @BeforeEach
    void setUp() {
        foodRepository = new InMemoryFoodRepository();
    }

    protected void assertFoodsAreEqual(Food food, Food saved) {
        assertThat(food.name()).isEqualTo(saved.name());
        assertThat(food.group()).isEqualTo(saved.group());
        assertThat(food.isInadequateFor()).containsExactlyElementsOf(saved.isInadequateFor());
    }
}

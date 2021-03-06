package com.github.lmartinez84.healthydiet;

import com.github.lmartinez84.healthydiet.recipe.Recipe;
import com.github.lmartinez84.healthydiet.recipe.RecipeBuilder;
import com.github.lmartinez84.healthydiet.recipe.exceptions.InvalidCaloriesRecipeException;
import com.github.lmartinez84.healthydiet.recipe.exceptions.InvalidNumberOfIngredientsRecipeException;
import com.github.lmartinez84.healthydiet.recipe.exceptions.RecipeWithoutStepsException;
import com.github.lmartinez84.healthydiet.recipe.step.Step;
import com.github.lmartinez84.healthydiet.user.domain.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@Execution(ExecutionMode.CONCURRENT)
class RecipeValidationTest {
    @Test
    void should_not_be_created_if_it_has_not_ingredients() {
        User itsAuthor = RecipeObjectMother.createUser("jp");
        assertThatExceptionOfType(InvalidNumberOfIngredientsRecipeException.class)
                .isThrownBy(this::aRecipeWithoutIngredients);
    }

    private Recipe aRecipeWithoutIngredients() {
        return RecipeBuilder
                .aRecipe()
                .name("aRecipe")
                .author(RecipeObjectMother.createUser("jp"))
                .collaborators(Set.of()).ingredients(Set.of())
                .steps(List.of(Step.of("aStep")))
                .build();
    }

    @Test
    void should_not_be_created_without_steps() {
        assertThatExceptionOfType(RecipeWithoutStepsException.class)
                .isThrownBy(this::aRecipeWithoutSteps);
    }

    private Recipe aRecipeWithoutSteps() {
        return RecipeBuilder
                .aRecipe()
                .name("aRecipe")
                .author(RecipeObjectMother.createUser("jp"))
                .ingredients(Set.of(RecipeObjectMother.createRandomIngredient()))
                .collaborators(Set.of()).ingredients(Set.of())
                .build();
    }

    @Test
    void calories_below_10_should_not_be_allowed() {
        assertThatExceptionOfType(InvalidCaloriesRecipeException.class)
                .isThrownBy(this::aRecipeWithLessThanTenCalories);
    }

    private Recipe aRecipeWithLessThanTenCalories() {
        return RecipeBuilder
                .aRecipe()
                .name("aRecipe")
                .author(RecipeObjectMother.createUser("jp"))
                .ingredients(Set.of(RecipeObjectMother.createRandomIngredient()))
                .collaborators(Set.of()).ingredients(Set.of())
                .steps(List.of(Step.of("aStep")))
                .calories(9)
                .build();
    }

    @Test
    void calories_above_5000_should_not_be_allowed() {
        User itsAuthor = RecipeObjectMother.createUser("jp");
        assertThatExceptionOfType(InvalidCaloriesRecipeException.class)
                .isThrownBy(this::aRecipeWith5000Calories);
    }

    private Recipe aRecipeWith5000Calories() {
        return RecipeBuilder
                .aRecipe()
                .name("aRecipe")
                .author(RecipeObjectMother.createUser("jp"))
                .ingredients(Set.of(RecipeObjectMother.createRandomIngredient()))
                .collaborators(Set.of()).ingredients(Set.of())
                .steps(List.of(Step.of("aStep")))
                .calories(5001)
                .build();
    }
}

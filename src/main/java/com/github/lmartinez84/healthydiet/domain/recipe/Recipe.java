package com.github.lmartinez84.healthydiet.domain.recipe;

import com.github.lmartinez84.healthydiet.domain.FoodInadequacy;
import com.github.lmartinez84.healthydiet.domain.Ingredient;
import com.github.lmartinez84.healthydiet.domain.RecipeWithoutStepsException;
import com.github.lmartinez84.healthydiet.domain.Step;
import com.github.lmartinez84.healthydiet.domain.recipe.exceptions.InvalidCaloriesRecipeException;
import com.github.lmartinez84.healthydiet.domain.recipe.exceptions.InvalidNumberOfIngredientsRecipeException;
import com.github.lmartinez84.healthydiet.domain.user.User;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

public class Recipe {
    public static final int MIN_CALORIES = 10;
    public static final int MAX_CALORIES = 5000;
    private final String name;
    private final User author;
    private final Set<User> collaborators;
    private final Set<Ingredient> ingredients;
    private final List<Step> steps;
    private final int calories;
    private List<Recipe> subRecipes;

    public Recipe(String name,
                  User author,
                  Set<User> collaborators,
                  int calories,
                  Set<Ingredient> ingredients,
                  List<Step> steps,
                  List<Recipe> subRecipes
    ) {
        this.subRecipes = subRecipes;
        this.calories = calories;
        this.name = name;
        this.author = author;
        this.collaborators = collaborators;
        this.ingredients = ingredients;
        this.steps = steps;
        this.validateRecipe(this);
    }

    private void validateRecipe(Recipe recipe) {
        hasAtLeastOneIngredient(recipe);
        hastAtLeastOneStep(recipe);
        caloriesAreBetween10and5000(recipe);
    }

    private void caloriesAreBetween10and5000(Recipe recipe) {
        if (recipe.calories() < MIN_CALORIES || recipe.calories() > MAX_CALORIES) {
            throw new InvalidCaloriesRecipeException();
        }
    }

    private void hastAtLeastOneStep(Recipe recipe) {
        if (recipe.steps.isEmpty()) {
            throw new RecipeWithoutStepsException();
        }
    }

    private void hasAtLeastOneIngredient(Recipe recipe) {
        if (recipe.ingredients.isEmpty()) {
            throw new InvalidNumberOfIngredientsRecipeException();
        }
    }

    public boolean isEditableBy(User user) {
        return isItsAuthor(user) || isACollaborator(user);
    }

    private boolean isACollaborator(User user) {
        return collaborators.contains(user);
    }

    private boolean isItsAuthor(User user) {
        return author.equals(user);
    }

    public List<Step> steps() {
        return Stream.concat(steps.stream(), subRecipesSteps())
                     .flatMap(Stream::of)
                     .collect(toList());
    }

    private Stream<Step> subRecipesSteps() {
        return subRecipes.stream().flatMap(recipe -> recipe.steps.stream());
    }

    public int calories() {
        return calories + subrecipesCalories();
    }

    private int subrecipesCalories() {
        return subRecipes.stream().mapToInt(Recipe::calories).sum();
    }

    public Set<FoodInadequacy> inadequateFor() {
        return Stream.concat(itsInadequancies(), subRecipesInadequancies())
                     .flatMap(Stream::of)
                     .collect(toSet());
    }


    private Stream<FoodInadequacy> subRecipesInadequancies() {
        return subRecipes.stream().map(Recipe::inadequateFor)
                         .flatMap(Collection::stream);
    }

    private Stream<FoodInadequacy> itsInadequancies() {
        return ingredients.stream()
                          .map(Ingredient::inadequateFor)
                          .flatMap(Collection::stream);
    }
}

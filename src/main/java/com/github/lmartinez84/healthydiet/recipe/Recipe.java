package com.github.lmartinez84.healthydiet.recipe;

import com.github.lmartinez84.healthydiet.food.FoodInadequacy;
import com.github.lmartinez84.healthydiet.recipe.exceptions.InvalidCaloriesRecipeException;
import com.github.lmartinez84.healthydiet.recipe.exceptions.InvalidNumberOfIngredientsRecipeException;
import com.github.lmartinez84.healthydiet.recipe.exceptions.RecipeWithoutStepsException;
import com.github.lmartinez84.healthydiet.recipe.ingredient.Ingredient;
import com.github.lmartinez84.healthydiet.recipe.step.Step;
import com.github.lmartinez84.healthydiet.shared.domain.Entity;
import com.github.lmartinez84.healthydiet.user.domain.User;

import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

public class Recipe extends Entity<RecipeId> {
    public static final int MIN_CALORIES = 10;
    public static final int MAX_CALORIES = 5000;

    protected final User author;
    protected final Set<User> collaborators;
    protected final Set<Ingredient> ingredients;
    protected final List<Step> steps;
    protected final int calories;
    protected String name;
    protected List<Recipe> subRecipes;

    public Recipe(String name,
                  User author,
                  Set<User> collaborators,
                  int calories,
                  Set<Ingredient> ingredients,
                  List<Step> steps,
                  List<Recipe> subRecipes
    ) {
        super(RecipeId.nullId());
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
        return subRecipes.stream()
                         .flatMap(recipe -> recipe.steps().stream());
    }

    public int calories() {
        return calories + subrecipesCalories();
    }

    private int subrecipesCalories() {
        return subRecipes.stream()
                         .mapToInt(Recipe::calories)
                         .sum();
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

    public Recipe copy() {
        return new Recipe(new String(this.name),
                          author,
                          Set.of(),
                          calories,
                          copyOfIngredients(),
                          copyOfSteps(),
                          copyOfSubRecipes());
    }

    protected List<Recipe> copyOfSubRecipes() {
        return this.subRecipes.stream()
                              .map(Recipe::copy)
                              .collect(toList());
    }

    protected List<Step> copyOfSteps() {
        return this.steps.stream()
                         .map(Step::copy)
                         .collect(toList());
    }

    protected Set<Ingredient> copyOfIngredients() {
        return this.ingredients.stream()
                               .map(Ingredient::copy)
                               .collect(toSet());
    }

    public User author() {
        return author;
    }

    public String name() {
        return name;
    }

    public Set<User> collaborators() {
        return Collections.unmodifiableSet(collaborators);
    }

    public Set<Ingredient> ingredients() {
        return Collections.unmodifiableSet(ingredients);
    }

    public void addCollaborator(User collaborator) {
        collaborators.add(collaborator);
    }

    public Recipe original() {
        return this;
    }

    public void name(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "author=" + author +
                ", collaborators=" + collaborators +
                ", ingredients=" + ingredients +
                ", steps=" + steps +
                ", calories=" + calories +
                ", name='" + name + '\'' +
                ", subRecipes=" + subRecipes +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Recipe recipe = (Recipe) o;
        return author.equals(recipe.author) &&
                name.equals(recipe.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(author, name);
    }
}

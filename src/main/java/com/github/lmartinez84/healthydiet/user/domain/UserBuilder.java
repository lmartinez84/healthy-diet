package com.github.lmartinez84.healthydiet.user.domain;

import com.github.lmartinez84.healthydiet.food.Food;
import com.github.lmartinez84.healthydiet.user.domain.dietary_requirement.DietaryRequirement;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class UserBuilder {
    private final Set<DietaryRequirement> dietaryRequirements = new HashSet<>();
    private String firstName;
    private String lastName;
    private String username;
    private double weight;
    private double height;
    private Routine routine;
    private LocalDate dateOfBirth;
    private Set<Food> favoriteFoods = new HashSet<>();
    private Set<Food> dislikedFoods = new HashSet<>();

    public static UserBuilder aUser() {
        return new UserBuilder();
    }

    public UserBuilder firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public UserBuilder lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public UserBuilder username(String username) {
        this.username = username;
        return this;
    }

    public UserBuilder weight(double weight) {
        this.weight = weight;
        return this;
    }

    public UserBuilder height(double height) {
        this.height = height;
        return this;
    }

    public UserBuilder dietaryRequirements(Set<DietaryRequirement> conditions) {
        this.dietaryRequirements.addAll(conditions);
        return this;
    }

    public UserBuilder routine(Routine routine) {
        this.routine = routine;
        return this;
    }

    public User build() {
        return new User(username, firstName, lastName, dateOfBirth, weight, height,
                        dietaryRequirements, routine, favoriteFoods);
    }

    public UserBuilder dateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        return this;
    }

    public UserBuilder favoriteFoods(Set<Food> favoriteFoods) {
        this.favoriteFoods.addAll(favoriteFoods);
        return this;
    }

    public UserBuilder dislikedFoods(Set<Food> bacon) {
        this.dislikedFoods.addAll(bacon);
        return this;
    }
}

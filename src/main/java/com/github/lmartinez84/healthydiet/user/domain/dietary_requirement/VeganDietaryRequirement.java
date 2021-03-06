package com.github.lmartinez84.healthydiet.user.domain.dietary_requirement;

import com.github.lmartinez84.healthydiet.food.FoodGroup;
import com.github.lmartinez84.healthydiet.food.FoodInadequacy;
import com.github.lmartinez84.healthydiet.user.domain.User;

public class VeganDietaryRequirement implements DietaryRequirement {

    public static final int REQUIRED_NUMBER_OF_FRUITS = 2;

    private static final FoodInadequacy inadecuacy = FoodInadequacy.VEGAN;

    @Override
    public FoodInadequacy inadequacy() {
        return inadecuacy;
    }

    @Override
    public boolean isCompensated(User user) {
        return itsFavoritesFoodsIncludeAtLeastToFruits(user);
    }

    @Override
    public void validate(User user) {

    }


    private boolean itsFavoritesFoodsIncludeAtLeastToFruits(User user) {
        return user.favoritesFoods()
                   .stream()
                   .filter(food -> food.isMemberOfGroup(FoodGroup.VEGETABLES_FRUITS_SEED))
                   .count() >= REQUIRED_NUMBER_OF_FRUITS;
    }
}

package com.github.lmartinez84.healthydiet.user.domain.dietary_requirement;

import com.github.lmartinez84.healthydiet.food.FoodGroup;
import com.github.lmartinez84.healthydiet.food.FoodInadequacy;
import com.github.lmartinez84.healthydiet.user.domain.User;

public class VegetarianDietaryRequirement implements DietaryRequirement {

    public static final int THIRTY = 30;
    private static final FoodInadequacy inadecuacy = FoodInadequacy.VEGETARIAN;

    @Override
    public FoodInadequacy inadequacy() {
        return inadecuacy;
    }

    @Override
    public boolean isCompensated(User user) {
        return isUnderThirty(user) || favoriteFoodsDoNotContainFats(user);
    }

    @Override
    public void validate(User user) {

    }


    private boolean favoriteFoodsDoNotContainFats(User user) {
        return user.favoritesFoods().stream()
                   .noneMatch(food -> food.isMemberOfGroup(FoodGroup.OIL_FAT_SUGAR));
    }

    private boolean isUnderThirty(User user) {
        return user.age() < THIRTY;
    }
}

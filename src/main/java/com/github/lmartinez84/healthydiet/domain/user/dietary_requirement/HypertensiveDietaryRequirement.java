package com.github.lmartinez84.healthydiet.domain.user.dietary_requirement;

import com.github.lmartinez84.healthydiet.domain.Routine;
import com.github.lmartinez84.healthydiet.domain.user.User;
import com.github.lmartinez84.healthydiet.domain.user.dietary_requirement.exceptions.HypertensiveHasNotFavoriteFoodUserException;

public class HypertensiveDietaryRequirement implements DietaryRequirement {
    @Override
    public boolean isCompensated(User user) {
        return user.routine() == Routine.INTENSIVE;
    }

    @Override
    public void validate(User user) {
        if (user.favoritesFoods().isEmpty()) {
            throw new HypertensiveHasNotFavoriteFoodUserException();
        }
    }


}

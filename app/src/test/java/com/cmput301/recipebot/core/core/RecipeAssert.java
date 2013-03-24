/*
 * Copyright 2013 Adam Saturna
 *  Copyright 2013 Brian Trinh
 *  Copyright 2013 Ethan Mykytiuk
 *  Copyright 2013 Prateek Srivastava (@f2prateek)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.cmput301.recipebot.core.core;

import com.cmput301.recipebot.model.Ingredient;
import com.cmput301.recipebot.model.Recipe;
import com.cmput301.recipebot.model.User;
import org.fest.assertions.api.AbstractAssert;
import org.fest.assertions.api.Assertions;

import java.util.Comparator;

public class RecipeAssert extends AbstractAssert<RecipeAssert, Recipe> {

    public RecipeAssert(Recipe actual) {
        super(actual, RecipeAssert.class);
    }

    public static RecipeAssert assertThat(Recipe actual) {
        return new RecipeAssert(actual);
    }

    public RecipeAssert hasId(String id) {
        isNotNull();
        Assertions.assertThat(actual.getId())
                .overridingErrorMessage("Expected recipe's id to be <%s> but was <%s>", id, actual.getId())
                .isEqualTo(id);
        return this;
    }

    public RecipeAssert hasDescription(String description) {
        isNotNull();
        Assertions.assertThat(actual.getDescription())
                .overridingErrorMessage("Expected recipe's description to be <%s> but was <%s>", description, actual.getDescription())
                .isEqualTo(description);
        return this;
    }

    public RecipeAssert hasUser(User user) {
        isNotNull();
        Assertions.assertThat(actual.getUser().getId())
                .overridingErrorMessage("Expected recipe's user's id to be <%s> but was <%s>", user.getId(), actual.getUser().getId());
        return this;
    }

    public RecipeAssert hasName(String name) {
        isNotNull();
        Assertions.assertThat(actual.getName())
                .overridingErrorMessage("Expected recipe's name to be <%s> but was <%s>", name, actual.getName())
                .isEqualTo(name);
        return this;
    }

    public RecipeAssert hasDirection(String direction) {
        isNotNull();
        Assertions.assertThat(actual.getDirections())
                .overridingErrorMessage("Expected recipe's directions to have <%s> but had <%s>", direction, actual.getDirections())
                .contains(direction);
        return this;
    }

    public RecipeAssert hasTag(String tag) {
        isNotNull();
        Assertions.assertThat(actual.getTags())
                .overridingErrorMessage("Expected recipe's tag to have <%s> but had <%s>", tag, actual.getTags())
                .contains(tag);
        return this;
    }

    public RecipeAssert hasPhoto(String photo) {
        isNotNull();
        Assertions.assertThat(actual.getPhotos())
                .overridingErrorMessage("Expected recipe's photo to contain <%s> but had <%s>", photo, actual.getPhotos())
                .contains(photo);
        return this;
    }

    public RecipeAssert hasIngredient(Ingredient ingredient) {
        isNotNull();
        Assertions.assertThat(actual.getIngredients())
                .overridingErrorMessage("Expected recipe's ingredients to have <%s> but had <%s>", ingredient, actual.getIngredients())
                .usingElementComparator(new IngredientComparator()).contains(ingredient);
        return this;
    }

    private class IngredientComparator implements Comparator<Ingredient> {

        @Override
        public int compare(Ingredient lhs, Ingredient rhs) {
            if (lhs.getName().compareToIgnoreCase(rhs.getName()) != 0) {
                return 1;
            }
            return 0;
        }
    }

}
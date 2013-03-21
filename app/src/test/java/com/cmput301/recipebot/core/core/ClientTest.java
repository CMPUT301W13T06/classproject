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

import com.cmput301.recipebot.client.ESClient;
import com.cmput301.recipebot.model.Ingredient;
import com.cmput301.recipebot.model.Recipe;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Comparator;

import static com.cmput301.recipebot.core.core.RecipeAssert.assertThat;
import static org.fest.assertions.api.Assertions.assertThat;

/**
 * Unit tests of client API
 */
public class ClientTest {

    private ESClient mClient;
    private Comparator<Recipe> recipeComparator;

    @Before
    public void setUp() throws Exception {
        mClient = new ESClient();
        // insertRecipesToServer();
        recipeComparator = new RecipeComparator();
    }

    /**
     * Test that a {@link Recipe} object can be inserted.
     * It also retrieves the object for testing.
     *
     * @throws Exception
     */
    @Test
    public void testInsert() throws Exception {
        Recipe r = getTestRecipe();
        boolean response = mClient.insertRecipe(r);
        assertThat(response).isTrue();
        Recipe actual = mClient.getRecipe(r.getId());
        assertThat(r).usingComparator(recipeComparator).isEqualTo(actual);
    }

    /**
     * Test that a {@link Recipe} object can be retrieved.
     *
     * @throws Exception
     */
    @Test
    public void testRetrieval() throws Exception {
        Recipe recipe1 = mClient.getRecipe("63ed5a87-3402-4148-8602-10cc8ff63fa7");
        assertThat(recipe1).hasName("Stir Fry").hasUser("Spiderman").hasDirection("Stir")
                .hasIngredient(new Ingredient("Lamb", "testing", 2f)).hasDescription("Not so healthy!");

        Recipe recipe2 = mClient.getRecipe("1d859b92-c1e3-4b48-9c23-f5472937403d");
        assertThat(recipe2).hasName("Omlette").hasUser("Bruce").hasDirection("Whip")
                .hasIngredient(new Ingredient("Milk", "dsa", 2f))
                .hasIngredient(new Ingredient("Oil", "sdsa", 2f)).hasDescription("Very Healthy");
    }

    /**
     * Make a new Recipe
     *
     * @return A test recipe.
     */
    private Recipe getTestRecipe() {
        Recipe recipe = new Recipe();
        recipe.setId("testing_id");
        recipe.setUser("Colonel Sanders");
        recipe.setName("Kentucky Fried Chicken");
        ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
        ingredients.add(new Ingredient("Chicken", "lb", 2f));
        ingredients.add(new Ingredient("Secret Spice #1", "tbsp.", 1f));
        ingredients.add(new Ingredient("Secret Spice #2", "tsp.", 1f));
        ingredients.add(new Ingredient("Buttermilk", "ml", 50f));
        recipe.setIngredients(ingredients);
        ArrayList<String> directions = new ArrayList<String>();
        directions.add("1. Mix");
        directions.add("2. Bake");
        directions.add("3. Eat");
        recipe.setDirections(directions);
        recipe.setPhotos(null);
        return recipe;
    }

    private class RecipeComparator implements Comparator<Recipe> {

        @Override
        public int compare(Recipe lhs, Recipe rhs) {
            if (lhs.getId().compareToIgnoreCase(rhs.getId()) != 0) {
                return 1;
            }
            if (lhs.getUser().compareToIgnoreCase(rhs.getUser()) != 0) {
                return 1;
            }
            if (lhs.getName().compareToIgnoreCase(rhs.getName()) != 0) {
                return 1;
            }
            return 0;
        }
    }
}

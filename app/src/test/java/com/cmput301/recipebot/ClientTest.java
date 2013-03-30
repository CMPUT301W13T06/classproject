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

package com.cmput301.recipebot;

import com.cmput301.recipebot.model.beans.Ingredient;
import com.cmput301.recipebot.model.beans.Recipe;
import com.cmput301.recipebot.model.beans.User;
import com.cmput301.recipebot.model.network.ESClient;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

import static com.cmput301.recipebot.RecipeAssert.assertThat;
import static com.cmput301.recipebot.util.TestDataSetGenerator.*;
import static org.fest.assertions.api.Assertions.assertThat;

/**
 * Unit tests of Elastic Search API
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
     * Test that a {@link Recipe} object can be updated.
     */
    @Test
    public void testUpdate() throws Exception {
        // Test the inital conditions. The user is randomised between runs, so we don't know what it is initially.
        Recipe recipe = mClient.getRecipe("46f1ea27-460c-4f5a-819b-33c0c333e6c2");
        assertThat(recipe).hasName("Baked Rice").hasDirection("Bake")
                .hasIngredient(new Ingredient("Beans", 2f, "dsa")).hasDescription("Unhealthy").hasTag("Fried")
                .hasPhoto(RECIPE_PHOTOS[6]);

        // make a new_user that is not the same as the old one.
        User original_user = recipe.getUser();
        User new_user = RECIPE_USERS[new Random().nextInt(RECIPE_USERS.length)];
        while (new_user.getId().compareTo(original_user.getId()) == 0) {
            new_user = RECIPE_USERS[new Random().nextInt(RECIPE_USERS.length)];
        }

        // Change some attribute
        recipe.setUser(new_user);
        assertThat(recipe).hasUser(new_user);
        boolean response = mClient.updateRecipe(recipe);
        assertThat(response).isTrue();

        //Verify only that attribute has changed in server, other remain same
        Recipe recipe2 = mClient.getRecipe("46f1ea27-460c-4f5a-819b-33c0c333e6c2");
        assertThat(recipe2).hasName("Baked Rice").hasDirection("Bake").hasUser(new_user)
                .hasIngredient(new Ingredient("Beans", 2f, "dsa")).hasDescription("Unhealthy").hasTag("Fried")
                .hasPhoto(RECIPE_PHOTOS[6]);
    }

    /**
     * Test that a {@link Recipe} object can be retrieved.
     */
    @Test
    public void testRetrieval() throws Exception {
        Recipe recipe1 = mClient.getRecipe("32a02a76-ed0d-4504-bfc4-ea996bfc36b3");
        assertThat(recipe1).hasName("Ravioli").hasUser(RECIPE_USERS[1]).hasDirection("Heat")
                .hasIngredient(new Ingredient("Beans", 2f, "testing")).hasIngredient(new Ingredient("Strawberry", 2f, "testing"))
                .hasIngredient(new Ingredient("Eggs", 2f, "testing"))
                .hasDescription("Not so healthy!").hasTag("Vegetarian").hasTag("Chicken");

        Recipe recipe2 = mClient.getRecipe("b0b6d762-296d-4cd6-980e-f629b96ed05e");
        assertThat(recipe2).hasName("Roasted Potatoes").hasUser(RECIPE_USERS[4]).hasDirection("Chop").hasDirection("Saute").hasDirection("Stir")
                .hasIngredient(new Ingredient("Fresh Greens", 2f, "dsa"))
                .hasIngredient(new Ingredient("Cream", 2f, "sdsa")).hasDescription("Unhealthy");
    }

    /**
     * Test that a {@link Recipe} object can be inserted.
     * It also retrieves the object for testing.
     */
    @Test
    public void testDelete() throws Exception {
        Recipe r = getTestRecipe();
        boolean response = mClient.insertRecipe(r);
        assertThat(response).isTrue();
        boolean deleteResponse = mClient.deleteRecipe(r.getId());
        assertThat(deleteResponse).isTrue();
        Recipe recipe1 = mClient.getRecipe(r.getId());
        assertThat(recipe1).isNull();
    }

    /**
     * Test that a {@link Recipe} can be searched by its name.
     */
    @Test
    public void testSearchByName() throws Exception {
        // Test the initial conditions, verify that the recipe exists on the server.
        Recipe recipe = mClient.getRecipe("064cca20-9bd9-43a3-8a57-98497b0acdf3");
        assertThat(recipe).hasName("Cream Soda").hasUser(RECIPE_USERS[2]).hasDirection("Blend")
                .hasIngredient(new Ingredient("Water", 2f, "testing")).hasDescription("Not so healthy!");

        ArrayList<Recipe> recipes = mClient.searchRecipes("Cream Soda");
        assertThat(recipes.size()).isGreaterThan(0); // We know this exists, but could be multiple (random).
        assertThat(recipes).usingElementComparator(recipeComparator).contains(recipe); // confirm that it contains our expected recipe

        recipes = mClient.searchRecipes("dsadssadasdas");
        assertThat(recipes.size()).isEqualTo(0); // We know this doesn't exist.
    }

    /**
     * Test that a {@link Recipe} can be searched by any one of its ingredients.
     */
    @Test
    public void testSearchByIndividualIngredient() throws Exception {
        Recipe recipe = mClient.getRecipe("90dbd393-c219-4e7b-aebd-39643f9cee5b");

        // Search with the every ingredient individually.
        for (Ingredient i : recipe.getIngredients()) {
            ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
            ingredients.add(i);
            ArrayList<Recipe> recipes = mClient.searchRecipes(ingredients, false);
            assertThat(recipes.size()).isGreaterThan(0); // We know this exists, but could be multiple (random).
            assertThat(recipes).usingElementComparator(recipeComparator).contains(recipe);
        }

        ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
        ingredients.add(new Ingredient("dsadsadasdasdasdada", 2f, "lb"));
        ArrayList<Recipe> recipes = mClient.searchRecipes(ingredients, false);
        assertThat(recipes.size()).isEqualTo(0); // We know this doesn't exist.
    }

    /**
     * Test that mulitple {@link Recipe} is given by a search.
     */
    @Test
    public void testSearchByIngredientsReturnsMultipleRecipes() throws Exception {
        Recipe recipe1 = mClient.getRecipe("be92092c-5419-4f69-a061-111e89ef4409");
        Recipe recipe2 = mClient.getRecipe("018aced9-9289-4c5c-8e98-ead216e30ae0");
        Ingredient ingredient = new Ingredient("Milk", 2f, "testing");
        assertThat(recipe1).hasIngredient(ingredient);
        assertThat(recipe2).hasIngredient(ingredient);

        ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
        ingredients.add(ingredient);
        ArrayList<Recipe> recipes = mClient.searchRecipes(ingredients, false);
        assertThat(recipes.size()).isGreaterThan(1); // We know at least two exist.
        assertThat(recipes).usingElementComparator(recipeComparator).contains(recipe1);
        assertThat(recipes).usingElementComparator(recipeComparator).contains(recipe2);
    }

    /**
     * Test that {@link Recipe} is given by a search of multiple ingredients.
     */
    @Test
    public void testSearchByMultipleIngredients() throws Exception {
        Recipe recipe = mClient.getRecipe("ad745bcd-8503-4716-ad57-8ee0f604ad65");
        Ingredient honey_garlic = new Ingredient("Honey Garlic", 2f, "testing");
        Ingredient bacon = new Ingredient("Bacon", 2f, "testing");
        Ingredient salt = new Ingredient("Salt", 2f, "testing");
        assertThat(recipe).hasIngredient(honey_garlic).hasIngredient(bacon).hasIngredient(salt);

        ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
        ingredients.add(honey_garlic);
        ingredients.add(bacon);
        ingredients.add(salt);

        // Test that we get at least this one (many recipes with one or more of these ingredients.
        ArrayList<Recipe> recipes = mClient.searchRecipes(ingredients, false);
        assertThat(recipes).usingElementComparator(recipeComparator).contains(recipe);

        // Test that we get only this one (only recipe with all ingredients)
        recipes = mClient.searchRecipes(ingredients, true);
        assertThat(recipes).usingElementComparator(recipeComparator).contains(recipe).hasSize(1);
    }

    /**
     * A method that generates a dataset that can be pushed to the server.
     * Our tests are reliant on this dataset. Everytime this function is used to generate a new dataset,
     * update our expected values.
     */
    private void insertRecipesToServer() {
        ArrayList<Recipe> mRecipeList;
        mRecipeList = generateRandomRecipes(50);
        for (Recipe recipe : mRecipeList) {
            mClient.insertRecipe(recipe);
        }
    }

    private class RecipeComparator implements Comparator<Recipe> {
        @Override
        public int compare(Recipe lhs, Recipe rhs) {
            if (lhs.getId().compareToIgnoreCase(rhs.getId()) != 0) {
                return 1;
            }
            if (lhs.getUser().getId().compareToIgnoreCase(rhs.getUser().getId()) != 0) {
                return 1;
            }
            if (lhs.getName().compareToIgnoreCase(rhs.getName()) != 0) {
                return 1;
            }
            return 0;
        }
    }
}

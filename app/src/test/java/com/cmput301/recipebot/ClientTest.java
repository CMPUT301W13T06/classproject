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
        insertRecipesToServer();
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
        Recipe recipe = mClient.getRecipe("ab5e645a-4a0d-4b4b-a811-4a75e6a8724b");
        assertThat(recipe).hasName("Hamburger").hasDirection("Stir").hasDirection("Whip").
                hasIngredient(new Ingredient("Beans", 2f, "")).hasIngredient(new Ingredient("Fish", 2f, "")).
                hasIngredient(new Ingredient("Sugar", 2f, "")).hasDescription("Not so healthy!").hasTag("Baked").
                hasTag("Southern");

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
        recipe = mClient.getRecipe("ab5e645a-4a0d-4b4b-a811-4a75e6a8724b");
        assertThat(recipe).hasName("Hamburger").hasDirection("Stir").hasDirection("Whip")
                .hasIngredient(new Ingredient("Beans", 2f, "")).hasIngredient(new Ingredient("Fish", 2f, ""))
                .hasIngredient(new Ingredient("Sugar", 2f, "")).hasDescription("Not so healthy!").hasTag("Baked")
                .hasTag("Southern");
    }

    /**
     * Test that a {@link Recipe} object can be retrieved.
     */
    @Test
    public void testRetrieval() throws Exception {
        Recipe recipe = mClient.getRecipe("b61ead92-cf0f-49fa-87bc-6ffebd12b7af");
        assertThat(recipe).hasName("Caesar Salad").hasUser(new User("2@gmail.com", "adam")).hasDirection("Blend")
                .hasDirection("Chop").hasDirection("Blend").hasIngredient(new Ingredient("Chicken", 2f, ""))
                .hasIngredient(new Ingredient("Cream", 2f, "")).hasDescription("Very Healthy").hasTag("Southern")
                .hasTag("Chicken").hasTag("Dairy");

        Recipe recipe2 = mClient.getRecipe("1cff157e-298d-40c2-ae88-481560f09e1c");
        assertThat(recipe2).hasName("Green Salad").hasDirection("Chop").hasDirection("Bake")
                .hasIngredient(new Ingredient("Bacon", 2f, ""))
                .hasIngredient(new Ingredient("Tofu", 2f, "")).hasDescription("Unhealthy");
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
        mClient.deleteRecipe(r.getId());
        Recipe recipe = mClient.getRecipe(r.getId());
        assertThat(recipe).isNull();
    }

    /**
     * Test that a {@link Recipe} can be searched by its name.
     */
    @Test
    public void testSearchByName() throws Exception {
        // Test the initial conditions, verify that the recipe exists on the server.
        Recipe recipe = mClient.getRecipe("c3764532-f057-48fa-8722-0f5f632d6152");
        assertThat(recipe).hasName("Fried Rice");

        ArrayList<Recipe> recipes = mClient.searchRecipes("Fried Rice");
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
        Recipe recipe = mClient.getRecipe("2479e0a8-dc4d-4118-b9f5-c09cb928a440");
        ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
        ingredients.add(new Ingredient("vodka", 2f, ""));
        ArrayList<Recipe> recipes = mClient.searchRecipes(ingredients, false);
        assertThat(recipes.size()).isGreaterThan(0); // We know this exists, but could be multiple (random).
        assertThat(recipes).usingElementComparator(recipeComparator).contains(recipe);

        ingredients = new ArrayList<Ingredient>();
        ingredients.add(new Ingredient("dsadsadasdasdasdada", 2f, ""));
        recipes = mClient.searchRecipes(ingredients, false);
        assertThat(recipes.size()).isEqualTo(0); // We know this doesn't exist.
    }

    /**
     * Test that multiple {@link Recipe} is given by a search.
     */
    @Test
    public void testSearchByIngredientsReturnsMultipleRecipes() throws Exception {
        Recipe recipe1 = mClient.getRecipe("2b52eb1c-ae43-4ef3-8e9e-f591d53df7d8");
        Recipe recipe2 = mClient.getRecipe("602b3a1d-8228-45a0-a08c-247522971005");
        Ingredient ingredient = new Ingredient("Bacon", 2f, "");
        assertThat(recipe1).hasIngredient(ingredient);
        assertThat(recipe2).hasIngredient(ingredient);

        ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
        ingredients.add(ingredient);
        ArrayList<Recipe> recipes = mClient.searchRecipes(ingredients, false);
        assertThat(recipes.size()).isGreaterThanOrEqualTo(2); // We know at least two exist.
        assertThat(recipes).usingElementComparator(recipeComparator).contains(recipe1);
        assertThat(recipes).usingElementComparator(recipeComparator).contains(recipe2);
    }

    /**
     * Test that {@link Recipe} is given by a search of multiple ingredients.
     */
    @Test
    public void testSearchByMultipleIngredients() throws Exception {
        Recipe recipe = mClient.getRecipe("e8c8a411-db82-4252-bd32-2125ec6992fe");
        Ingredient vodka = new Ingredient("Vodka", 2f, "");
        Ingredient bacon = new Ingredient("Bacon", 2f, "");
        Ingredient salt = new Ingredient("Salt", 2f, "");
        Ingredient strawberry = new Ingredient("Strawberry", 2f, "");
        assertThat(recipe).hasIngredient(vodka).hasIngredient(bacon).hasIngredient(salt).hasIngredient(strawberry);

        ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
        ingredients.add(vodka);
        ingredients.add(bacon);
        ingredients.add(salt);
        ingredients.add(strawberry);

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
        mRecipeList = generateRandomRecipes(100);
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

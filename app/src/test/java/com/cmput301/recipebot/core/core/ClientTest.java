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

import java.util.*;

import static com.cmput301.recipebot.core.core.RecipeAssert.assertThat;
import static org.fest.assertions.api.Assertions.assertThat;

/**
 * Unit tests of client API
 */
public class ClientTest {

    private static final String[] RECIPE_NAMES = {"Chicken Parmesan", "Kentucky Fried Chicken", "Green Salad", "Kahlua",
            "Stir Fry", "Fried Rice", "Hamburger", "Mocha", "Cream Soda", "Omelette's", "Chicken Wings", "Caesar Salad",
            "Steak", "Chilli", "Baked Rice", "Roasted Potatoes", "Chicken Satay", "Ravioli", "Corn on the Cob"};
    private static final String[] RECIPE_DESCRIPTIONS = {"Very Healthy", "Not so healthy!", "Unhealthy"};
    private static final String[] RECIPE_INGREDIENTS = {"Chicken", "Butter", "Water", "Fresh Vegetables",
            "Fresh Greens", "Vodka", "Oil", "Bun", "Salt", "Lamb", "Peas", "Fish", "Beans", "Tofu", "Cream",
            "Sugar", "Rhubarb", "Milk", "Eggs", "Strawberry", "Honey Garlic", "Corn", "Beans", "Kale", "Bacon"};
    private static final String[] RECIPE_DIRECTIONS = {"Bake", "Mix", "Shake", "Blend", "Eat", "Heat",
            "Fry", "Saute", "Mash", "Steam", "Stir", "Whip", "Chop", "Blend", "Boil", "Grill"};
    private static final String[] RECIPE_USERS = {"Prateek", "Adam", "Ethan", "Brian", "Batman", "Superman",
            "Spiderman", "Green Lantern", "Bruce", "Clark"};
    private static final int TEST_SIZES = 50;

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
     * Test that a {@link Recipe} object can be updated.
     *
     * @throws Exception
     */
    @Test
    public void testUpdate() throws Exception {
        // Test the inital conditions. The user is randomised between runs.
        Recipe recipe = mClient.getRecipe("ab3fa2b0-7c52-43b3-9e27-76a7b8959751");
        assertThat(recipe).hasName("Stir Fry").hasDirection("Boil").hasDirection("Bake")
                .hasIngredient(new Ingredient("Peas", "testing", 2f)).hasDescription("Not so healthy!");

        // make a new_user that is not the same as the old one.
        String original_user = recipe.getUser();
        String new_user = new String(original_user);
        while (new_user.compareToIgnoreCase(original_user) == 0) {
            new_user = RECIPE_USERS[new Random().nextInt(RECIPE_USERS.length)];
        }

        // Change some attribute
        recipe.setUser(new_user);
        assertThat(recipe).hasUser(new_user);
        boolean response = mClient.updateRecipe(recipe);
        assertThat(response).isTrue();

        //Verify only that attribute has changed in server, other remain same
        Recipe recipe2 = mClient.getRecipe("ab3fa2b0-7c52-43b3-9e27-76a7b8959751");
        assertThat(recipe2).hasName("Stir Fry").hasUser(new_user).hasDirection("Boil").hasDirection("Bake")
                .hasIngredient(new Ingredient("Peas", "testing", 2f)).hasDescription("Not so healthy!");
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
     * Test that a {@link Recipe} object can be inserted.
     * It also retrieves the object for testing.
     *
     * @throws Exception
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

    /**
     * A method that generates a dataset that can be pushed to the server.
     * Our tests are reliant on this dataset.
     */
    private void insertRecipesToServer() {
        List<Recipe> mRecipeList;

        mRecipeList = generateTestRecipes();

        for (Recipe recipe : mRecipeList) {
            mClient.insertRecipe(recipe);
        }
    }

    /**
     * Generate some random recipes.
     */
    private List<Recipe> generateTestRecipes() {
        List<Recipe> recipeList = new ArrayList<Recipe>();

        Random random = new Random();

        for (int i = 0; i < TEST_SIZES; i++) {
            Recipe r = new Recipe();
            r.setId(UUID.randomUUID().toString());
            r.setDescription(RECIPE_DESCRIPTIONS[random.nextInt(RECIPE_DESCRIPTIONS.length)]);
            r.setUser(RECIPE_USERS[random.nextInt(RECIPE_USERS.length)]);
            r.setName(RECIPE_NAMES[random.nextInt(RECIPE_NAMES.length)]);
            ArrayList<String> directions = new ArrayList<String>();
            for (int j = 0; j < random.nextInt(3) + 1; j++) {
                directions.add(RECIPE_DIRECTIONS[random.nextInt(RECIPE_DIRECTIONS.length)]);
            }
            r.setDirections(directions);
            ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
            for (int j = 0; j < random.nextInt(6) + 1; j++) {
                String name = RECIPE_INGREDIENTS[random.nextInt(RECIPE_INGREDIENTS.length)];
                Ingredient ingredient = new Ingredient(name, "testing", 4f);
                ingredients.add(ingredient);
            }
            r.setIngredients(ingredients);
            r.setPhotos(null);
            recipeList.add(r);
        }

        return recipeList;
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

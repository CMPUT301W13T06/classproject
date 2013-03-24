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
import com.cmput301.recipebot.model.User;
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
    private static final User[] RECIPE_USERS = {new User("1@gmail.com", "prateek"), new User("2@gmail.com", "adam"), new User("3@gmail.com", "ethan"),
            new User("4@gmail.com", "brian"), new User("5@gmail.com", "bruce"), new User("6@gmail.com", "clark")};
    private static final String[] RECIPE_TAGS = {"Chicken", "Dairy", "Vegetarian", "Southern", "Baked", "Fried"};

    private static final String[] RECIPE_PHOTOS = new String[]{
            "https://lh6.googleusercontent.com/-jZgveEqb6pg/T3R4kXScycI/AAAAAAAAAE0/xQ7CvpfXDzc/s1024/sample_image_01.jpg",
            "https://lh4.googleusercontent.com/-K2FMuOozxU0/T3R4lRAiBTI/AAAAAAAAAE8/a3Eh9JvnnzI/s1024/sample_image_02.jpg",
            "https://lh5.googleusercontent.com/-SCS5C646rxM/T3R4l7QB6xI/AAAAAAAAAFE/xLcuVv3CUyA/s1024/sample_image_03.jpg",
            "https://lh6.googleusercontent.com/-f0NJR6-_Thg/T3R4mNex2wI/AAAAAAAAAFI/45oug4VE8MI/s1024/sample_image_04.jpg",
            "https://lh3.googleusercontent.com/-n-xcJmiI0pg/T3R4mkSchHI/AAAAAAAAAFU/EoiNNb7kk3A/s1024/sample_image_05.jpg",
            "https://lh3.googleusercontent.com/-X43vAudm7f4/T3R4nGSChJI/AAAAAAAAAFk/3bna6D-2EE8/s1024/sample_image_06.jpg",
            "https://lh5.googleusercontent.com/-MpZneqIyjXU/T3R4nuGO1aI/AAAAAAAAAFg/r09OPjLx1ZY/s1024/sample_image_07.jpg",
            "https://lh6.googleusercontent.com/-ql3YNfdClJo/T3XvW9apmFI/AAAAAAAAAL4/_6HFDzbahc4/s1024/sample_image_08.jpg",
            "https://lh5.googleusercontent.com/-Pxa7eqF4cyc/T3R4oasvPEI/AAAAAAAAAF0/-uYDH92h8LA/s1024/sample_image_09.jpg",
            "https://lh4.googleusercontent.com/-Li-rjhFEuaI/T3R4o-VUl4I/AAAAAAAAAF8/5E5XdMnP1oE/s1024/sample_image_10.jpg",
            "https://lh5.googleusercontent.com/-_HU4fImgFhA/T3R4pPVIwWI/AAAAAAAAAGA/0RfK_Vkgth4/s1024/sample_image_11.jpg",
            "https://lh6.googleusercontent.com/-0gnNrVjwa0Y/T3R4peGYJwI/AAAAAAAAAGU/uX_9wvRPM9I/s1024/sample_image_12.jpg",
            "https://lh3.googleusercontent.com/-HBxuzALS_Zs/T3R4qERykaI/AAAAAAAAAGQ/_qQ16FaZ1q0/s1024/sample_image_13.jpg",
            "https://lh4.googleusercontent.com/-cKojDrARNjQ/T3R4qfWSGPI/AAAAAAAAAGY/MR5dnbNaPyY/s1024/sample_image_14.jpg",
            "https://lh3.googleusercontent.com/-WujkdYfcyZ8/T3R4qrIMGUI/AAAAAAAAAGk/277LIdgvnjg/s1024/sample_image_15.jpg",
            "https://lh6.googleusercontent.com/-FMHR7Vy3PgI/T3R4rOXlEKI/AAAAAAAAAGs/VeXrDNDBkaw/s1024/sample_image_16.jpg",
            "https://lh4.googleusercontent.com/-mrR0AJyNTH0/T3R4rZs6CuI/AAAAAAAAAG0/UE1wQqCOqLA/s1024/sample_image_17.jpg",
            "https://lh6.googleusercontent.com/-z77w0eh3cow/T3R4rnLn05I/AAAAAAAAAG4/BaerfWoNucU/s1024/sample_image_18.jpg",
            "https://lh5.googleusercontent.com/-aWVwh1OU5Bk/T3R4sAWw0yI/AAAAAAAAAHE/4_KAvJttFwA/s1024/sample_image_19.jpg",
            "https://lh6.googleusercontent.com/-q-js52DMnWQ/T3R4tZhY2sI/AAAAAAAAAHM/A8kjp2Ivdqg/s1024/sample_image_20.jpg",
            "https://lh5.googleusercontent.com/-_jIzvvzXKn4/T3R4t7xpdVI/AAAAAAAAAHU/7QC6eZ10jgs/s1024/sample_image_21.jpg",
            "https://lh3.googleusercontent.com/-lnGi4IMLpwU/T3R4uCMa7vI/AAAAAAAAAHc/1zgzzz6qTpk/s1024/sample_image_22.jpg",
    };

    private static final int TEST_SIZES = 50;

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
        assertThat(recipe1).hasName("Stir Fry").hasUser(RECIPE_USERS[2]).hasDirection("Stir")
                .hasIngredient(new Ingredient("Lamb", "testing", 2f)).hasDescription("Not so healthy!");

        Recipe recipe2 = mClient.getRecipe("1d859b92-c1e3-4b48-9c23-f5472937403d");
        assertThat(recipe2).hasName("Omlette").hasUser(RECIPE_USERS[1]).hasDirection("Whip")
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
     * Test that a {@link Recipe} can be searched by its name.
     * TODO : add a unique recipe (that will return only one).
     *
     * @throws Exception
     */
    @Test
    public void testSearchByName() throws Exception {
        // Test the initial conditions, verify that the recipe exists on the server.
        Recipe recipe = mClient.getRecipe("ac864659-9f55-480c-b744-8dbf0c180911");
        assertThat(recipe).hasName("Cream Soda").hasUser(RECIPE_USERS[2]).hasDirection("Mix").hasDirection("Grill")
                .hasIngredient(new Ingredient("Butter", "testing", 2f)).hasDescription("Very Healthy");

        List<Recipe> recipes = mClient.searchRecipes("Cream Soda");
        assertThat(recipes.size()).isGreaterThan(0); // We know this exists, but could be multiple (random).
        assertThat(recipes).usingElementComparator(recipeComparator).contains(recipe); // confirm that it contains our expected recipe

        recipes = mClient.searchRecipes("dsadssadasdas");
        assertThat(recipes.size()).isEqualTo(0); // We know this doesn't exist.
    }

    /**
     * Test that a {@link Recipe} can be searched by any one of its ingredients.
     *
     * @throws Exception
     */
    @Test
    public void testSearchByIndividualIngredient() throws Exception {
        Recipe recipe = mClient.getRecipe("3df532d5-f141-493b-9cec-dab02d2b3210");

        // Search with the every ingredient individually.
        for (Ingredient i : recipe.getIngredients()) {
            ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
            ingredients.add(i);
            List<Recipe> recipes = mClient.searchRecipes(ingredients, false);
            assertThat(recipes.size()).isGreaterThan(0); // We know this exists, but could be multiple (random).
            assertThat(recipes).usingElementComparator(recipeComparator).contains(recipe);
        }

        ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
        ingredients.add(new Ingredient("dsadsadasdasdasdada", "lb", 2f));
        List<Recipe> recipes = mClient.searchRecipes(ingredients, false);
        assertThat(recipes.size()).isEqualTo(0); // We know this doesn't exist.
    }

    /**
     * Test that mulitple {@link Recipe} is given by a search.
     *
     * @throws Exception
     */
    @Test
    public void testSearchByIngredientsReturnsMultipleRecipes() throws Exception {
        Recipe recipe1 = mClient.getRecipe("3df532d5-f141-493b-9cec-dab02d2b3210");
        Recipe recipe2 = mClient.getRecipe("e5bb4189-5e5b-4659-9030-ec99983b69b5");
        Ingredient ingredient = new Ingredient("Bacon", "testing", 2f);
        assertThat(recipe1).hasIngredient(ingredient);
        assertThat(recipe2).hasIngredient(ingredient);

        List<Ingredient> ingredients = new ArrayList<Ingredient>();
        ingredients.add(ingredient);
        List<Recipe> recipes = mClient.searchRecipes(ingredients, false);
        assertThat(recipes.size()).isGreaterThan(1); // We know at least two exist.
        assertThat(recipes).usingElementComparator(recipeComparator).contains(recipe1);
        assertThat(recipes).usingElementComparator(recipeComparator).contains(recipe2);
    }

    /**
     * Test that mulitple {@link Recipe} is given by a search.
     *
     * @throws Exception
     */
    @Test
    public void testSearchByMultipleIngredients() throws Exception {
        Recipe recipe = mClient.getRecipe("268d295f-2829-4d8c-b0fa-d3ffedad03e7");
        Ingredient vodka = new Ingredient("Vodka", "testing", 2f);
        Ingredient butter = new Ingredient("Butter", "testing", 2f);
        Ingredient peas = new Ingredient("Peas", "testing", 2f);
        Ingredient fish = new Ingredient("Fish", "testing", 2f);
        assertThat(recipe).hasIngredient(vodka).hasIngredient(butter).hasIngredient(peas).hasIngredient(fish);

        List<Ingredient> ingredients = new ArrayList<Ingredient>();
        ingredients.add(vodka);
        ingredients.add(butter);
        ingredients.add(peas);
        ingredients.add(fish);

        // Test that we get at least this one (many recipes with one or more of these ingredients.
        List<Recipe> recipes = mClient.searchRecipes(ingredients, false);
        assertThat(recipes).usingElementComparator(recipeComparator).contains(recipe);

        // Test that we get only this one (only recipe with all ingredients)
        recipes = mClient.searchRecipes(ingredients, true);
        assertThat(recipes).usingElementComparator(recipeComparator).contains(recipe).hasSize(1);
    }

    /**
     * Make a new Recipe
     *
     * @return A test recipe.
     */
    private Recipe getTestRecipe() {
        String id = "testing_id";
        String name = "Kentucky Fried Chicken";
        String description = "Fried Chicken";
        User user = new User("colonel@kfc.com", "Colonel Sanders");
        ArrayList<String> directions = new ArrayList<String>();
        directions.add("1. Mix");
        directions.add("2. Bake");
        directions.add("3. Eat");
        ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
        ingredients.add(new Ingredient("Chicken", "lb", 2f));
        ingredients.add(new Ingredient("Secret Spice #1", "tbsp.", 1f));
        ingredients.add(new Ingredient("Secret Spice #2", "tsp.", 1f));
        ingredients.add(new Ingredient("Buttermilk", "ml", 50f));
        ArrayList<String> photos = new ArrayList<String>();
        for (int i = 0; i < 5; i++) {
            photos.add(RECIPE_PHOTOS[i]);
        }
        ArrayList<String> tags = new ArrayList<String>();
        tags.add("chicken, fried, southern");

        Recipe recipe = new Recipe(id, name, description, user, ingredients, directions, photos, tags);
        return recipe;
    }

    /**
     * A method that generates a dataset that can be pushed to the server.
     * Our tests are reliant on this dataset. Everytime this function is used to generate a new dataset,
     * update our expected values.
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
            String id = UUID.randomUUID().toString();
            String name = RECIPE_NAMES[random.nextInt(RECIPE_NAMES.length)];
            String description = RECIPE_DESCRIPTIONS[random.nextInt(RECIPE_DESCRIPTIONS.length)];
            User user = RECIPE_USERS[random.nextInt(RECIPE_USERS.length)];
            ArrayList<String> directions = new ArrayList<String>();
            for (int j = 0; j < random.nextInt(3) + 1; j++) {
                directions.add(RECIPE_DIRECTIONS[random.nextInt(RECIPE_DIRECTIONS.length)]);
            }
            ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
            for (int j = 0; j < random.nextInt(6) + 1; j++) {
                Ingredient ingredient = new Ingredient(RECIPE_INGREDIENTS[random.nextInt(RECIPE_INGREDIENTS.length)], "testing", 4f);
                ingredients.add(ingredient);
            }
            ArrayList<String> photos = new ArrayList<String>();
            for (int j = 0; j < random.nextInt(6) + 1; j++) {
                photos.add(RECIPE_PHOTOS[random.nextInt(RECIPE_PHOTOS.length)]);
            }
            ArrayList<String> tags = new ArrayList<String>();
            for (int j = 0; j < random.nextInt(6) + 1; j++) {
                tags.add(RECIPE_TAGS[random.nextInt(RECIPE_TAGS.length)]);
            }

            Recipe recipe = new Recipe(id, name, description, user, ingredients, directions, photos, tags);
            recipeList.add(recipe);
        }

        return recipeList;
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

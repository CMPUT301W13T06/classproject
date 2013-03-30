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

package com.cmput301.recipebot.util;

import com.cmput301.recipebot.model.beans.Ingredient;
import com.cmput301.recipebot.model.beans.Recipe;
import com.cmput301.recipebot.model.beans.User;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

public class TestDataSetGenerator {

    public static final String[] RECIPE_NAMES = {"Chicken Parmesan", "Kentucky Fried Chicken", "Green Salad", "Kahlua",
            "Stir Fry", "Fried Rice", "Hamburger", "Mocha", "Cream Soda", "Omelette's", "Chicken Wings", "Caesar Salad",
            "Steak", "Chilli", "Baked Rice", "Roasted Potatoes", "Chicken Satay", "Ravioli", "Corn on the Cob",
            "Pancakes", "Waffles", "Cake", "Pie", "Pizza"};
    public static final String[] RECIPE_DESCRIPTIONS = {"Very Healthy", "Not so healthy!", "Unhealthy"};
    public static final String[] RECIPE_INGREDIENTS = {"Chicken", "Butter", "Water", "Fresh Vegetables",
            "Fresh Greens", "Vodka", "Oil", "Bun", "Salt", "Lamb", "Peas", "Fish", "Beans", "Tofu", "Cream",
            "Sugar", "Rhubarb", "Milk", "Eggs", "Strawberry", "Honey Garlic", "Corn", "Beans", "Kale", "Bacon"};
    public static final String[] RECIPE_DIRECTIONS = {"Bake", "Mix", "Shake", "Blend", "Eat", "Heat",
            "Fry", "Saute", "Mash", "Steam", "Stir", "Whip", "Chop", "Blend", "Boil", "Grill"};
    public static final User[] RECIPE_USERS = {new User("1@gmail.com", "prateek"), new User("2@gmail.com", "adam"),
            new User("3@gmail.com", "ethan"), new User("4@gmail.com", "brian"), new User("5@gmail.com", "bruce"),
            new User("6@gmail.com", "clark")};
    public static final String[] RECIPE_TAGS = {"Chicken", "Dairy", "Vegetarian", "Southern", "Baked", "Fried", "Breakfast", "Dinner"};
    public static final String[] RECIPE_PHOTOS = new String[]{
            "http://yrfn.ca/wp-content/uploads/2010/11/good-food-box.jpg",
            "http://www.cosmopolitanclublahore.com/wp-content/uploads/2011/06/fast-food-burger.jpg",
            "https://lh3.ggpht.com/-4fwd4r_Fi3s/TpxSc3uPFNI/AAAAAAAABA4/FHvNdzLiycA/s1600/food.jpg",
            "https://upload.wikimedia.org/wikipedia/commons/4/48/Korean.food-Hoe.naengmyeon-01.jpg",
            "http://trentarthur.ca/wp-content/uploads/2012/11/Foods.jpg",
            "http://visportsnutrition.ca/wp-content/uploads/2013/02/Close-Up-Kiwi-Fruit-Salad.jpg",
            "https://lh3.ggpht.com/_v45-TBo7M6U/TAiPRuojI_I/AAAAAAAAJto/39o51Eh9tYI/s1600/Hot+Food01.jpg",
            "http://www.realfoods.co.uk/Uploads/kim/fresh-fruit-and-vegetables.jpg",
            "http://www.gx94radio.com/blogs/middays/wp-content/upLoads/food.jpg",
            "http://www.ctvnews.ca/polopoly_fs/1.840137!/httpImage/image.jpg_gen/derivatives/landscape_960/image.jpg",
            "https://lh3.ggpht.com/_v45-TBo7M6U/TA7okOMBe6I/AAAAAAAAJ0c/bwm_U9nNmI8/s1600/Food02.jpg",
            "http://www.theweathernetwork.com/common/images/uploadnewstorm/Food_art-7-18684.jpg",
            "http://i-cdn.apartmenttherapy.com/uimages/kitchen/2008_04_15-PlaneFood.jpg",
            "http://blogs.uoregon.edu/natewoodburyaad250/files/2012/10/PSD_Food_illustrations_3190_pancakes_with_butter-1wi1tz5.jpg",
            "http://www.thefooddudes.com/_files/canvas/close-ups/larges/food-dudes-dingers-3.jpg",
            "https://upload.wikimedia.org/wikipedia/commons/5/51/Korean.food-Kimbap-03.jpg"
    };

    /**
     * Generate some random recipes.
     */
    public static ArrayList<Recipe> generateRandomRecipes(int count) {
        ArrayList<Recipe> recipeList = new ArrayList<Recipe>();

        for (int i = 0; i < count; i++) {
            recipeList.add(generateRandomRecipe());
        }

        return recipeList;
    }

    public static Recipe generateRandomRecipe() {
        String id = UUID.randomUUID().toString();
        Random random = new Random();
        String name = RECIPE_NAMES[random.nextInt(RECIPE_NAMES.length)];
        String description = RECIPE_DESCRIPTIONS[random.nextInt(RECIPE_DESCRIPTIONS.length)];
        User user = RECIPE_USERS[random.nextInt(RECIPE_USERS.length)];
        ArrayList<String> directions = new ArrayList<String>();
        for (int j = 0; j < random.nextInt(3) + 1; j++) {
            directions.add(RECIPE_DIRECTIONS[random.nextInt(RECIPE_DIRECTIONS.length)]);
        }
        ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
        for (int j = 0; j < random.nextInt(6) + 1; j++) {
            Ingredient ingredient = new Ingredient(RECIPE_INGREDIENTS[random.nextInt(RECIPE_INGREDIENTS.length)], 4f, "testing");
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

        return new Recipe(id, name, description, user, ingredients, directions, photos, tags);
    }

    /**
     * Make a new Recipe
     *
     * @return A test recipe.
     */
    public static Recipe getTestRecipe() {
        String id = "testing_id";
        String name = "Kentucky Fried Chicken";
        String description = "Fried Chicken";
        User user = new User("colonel@kfc.com", "Colonel Sanders");
        ArrayList<String> directions = new ArrayList<String>();
        directions.add("1. Mix");
        directions.add("2. Bake");
        directions.add("3. Eat");
        ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
        ingredients.add(new Ingredient("Chicken", 2f, "lb"));
        ingredients.add(new Ingredient("Secret Spice #1", 1f, "tbsp."));
        ingredients.add(new Ingredient("Secret Spice #2", 1f, "tsp."));
        ingredients.add(new Ingredient("Buttermilk", 50f, "ml"));
        ArrayList<String> photos = new ArrayList<String>();
        for (int i = 0; i < 5; i++) {
            photos.add(RECIPE_PHOTOS[i]);
        }
        ArrayList<String> tags = new ArrayList<String>();
        tags.add("chicken, fried, southern");

        Recipe recipe = new Recipe(id, name, description, user, ingredients, directions, photos, tags);
        return recipe;
    }

}

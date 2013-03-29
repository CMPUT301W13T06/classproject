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

import com.cmput301.recipebot.model.Ingredient;
import com.cmput301.recipebot.model.Recipe;
import com.cmput301.recipebot.model.User;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

public class TestDataSetGenerator {

    public static final String[] RECIPE_NAMES = {"Chicken Parmesan", "Kentucky Fried Chicken", "Green Salad", "Kahlua",
            "Stir Fry", "Fried Rice", "Hamburger", "Mocha", "Cream Soda", "Omelette's", "Chicken Wings", "Caesar Salad",
            "Steak", "Chilli", "Baked Rice", "Roasted Potatoes", "Chicken Satay", "Ravioli", "Corn on the Cob"};
    public static final String[] RECIPE_DESCRIPTIONS = {"Very Healthy", "Not so healthy!", "Unhealthy"};
    public static final String[] RECIPE_INGREDIENTS = {"Chicken", "Butter", "Water", "Fresh Vegetables",
            "Fresh Greens", "Vodka", "Oil", "Bun", "Salt", "Lamb", "Peas", "Fish", "Beans", "Tofu", "Cream",
            "Sugar", "Rhubarb", "Milk", "Eggs", "Strawberry", "Honey Garlic", "Corn", "Beans", "Kale", "Bacon"};
    public static final String[] RECIPE_DIRECTIONS = {"Bake", "Mix", "Shake", "Blend", "Eat", "Heat",
            "Fry", "Saute", "Mash", "Steam", "Stir", "Whip", "Chop", "Blend", "Boil", "Grill"};
    public static final User[] RECIPE_USERS = {new User("1@gmail.com", "prateek"), new User("2@gmail.com", "adam"), new User("3@gmail.com", "ethan"),
            new User("4@gmail.com", "brian"), new User("5@gmail.com", "bruce"), new User("6@gmail.com", "clark")};
    public static final String[] RECIPE_TAGS = {"Chicken", "Dairy", "Vegetarian", "Southern", "Baked", "Fried"};
    public static final String[] RECIPE_PHOTOS = new String[]{
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

    /**
     * Generate some random recipes.
     */
    public static ArrayList<Recipe> generateTestRecipes(int count) {
        ArrayList<Recipe> recipeList = new ArrayList<Recipe>();

        Random random = new Random();

        for (int i = 0; i < count; i++) {
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

}

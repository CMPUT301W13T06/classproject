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
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests of client API
 */
public class ClientTest {

    /**
     * Test that a {@link Recipe} object can be created.
     *
     * @throws Exception
     */
    @Test
    public void testInsert() throws Exception {
        Recipe recipe = new Recipe();
        recipe.setId(999);
        recipe.setUser("Emily");
        recipe.setName("Cheese Cake");
        ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
        ingredients.add(new Ingredient("egg", "nos", 2f));
        ingredients.add(new Ingredient("cheese", "slices", 2f));
        ingredients.add(new Ingredient("milk", "ml", 500f));
        recipe.setIngredients(ingredients);
        ArrayList<String> directions = new ArrayList<String>();
        directions.add("mix and bake");
        recipe.setDirections(directions);
        recipe.setPhotos(null);
        boolean response = ESClient.insertRecipe(recipe);

        assertTrue(response);

        Recipe recipe2 = ESClient.getRecipe(999);
        assertEquals("Emily", recipe2.getUser());
        assertEquals("Cheese Cake", recipe2.getName());
    }

    /**
     * Test that a {@link Recipe} object can be retrieved.
     *
     * @throws Exception
     */
    @Test
    public void testRetrieval() throws Exception {
        Recipe recipe = ESClient.getRecipe(999);
        assertEquals("Emily", recipe.getUser());
        assertEquals("Cheese Cake", recipe.getName());
    }
}

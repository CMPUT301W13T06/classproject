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

    @Test
    public void testInsert() throws Exception {
        Recipe.Builder builder = new Recipe.Builder();
        builder.setId(999);
        builder.setUser("Emily");
        builder.setName("Cheese Cake");
        ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
        ingredients.add(new Ingredient("egg", "nos", 2f));
        ingredients.add(new Ingredient("cheese", "slices", 2f));
        ingredients.add(new Ingredient("milk", "ml", 500f));
        builder.setIngredients(ingredients);
        ArrayList<String> directions = new ArrayList<String>();
        directions.add("mix and bake");
        builder.setDirections(directions);
        builder.setImages(null);
        Recipe recipe = builder.build();
        boolean response = ESClient.insertRecipe(recipe);

        assertTrue(response);

        Recipe recipe2 = ESClient.getRecipe(999);
        assertEquals("Emily", recipe2.getUser());
        assertEquals("Cheese Cake", recipe2.getName());
    }

    @Test
    public void testRetrieval() throws Exception {
        Recipe recipe = ESClient.getRecipe(999);
        assertEquals("Emily", recipe.getUser());
        assertEquals("Cheese Cake", recipe.getName());
    }
}

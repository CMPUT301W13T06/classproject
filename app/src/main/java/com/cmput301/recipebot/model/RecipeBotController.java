package com.cmput301.recipebot.model;

import android.content.Context;
import com.cmput301.recipebot.client.ESClient;

import java.util.List;

/**
 * A controller class that makes it transparent when switching betwen network and local calls.
 */
public class RecipeBotController {

    DatabaseHelper dbHelper;
    ESClient esClient;

    public RecipeBotController(Context context) {
        dbHelper = new DatabaseHelper(context);
        esClient = new ESClient();
    }

    public List<Ingredient> loadPantry() {
        return dbHelper.getAllPantryItems();
    }

    public void insertPantryItem(Ingredient ingredient) {
        dbHelper.insertPantryItem(ingredient);
    }

    public void deletePantryItem(String name) {
        dbHelper.deletePantryItem(name);
    }

}

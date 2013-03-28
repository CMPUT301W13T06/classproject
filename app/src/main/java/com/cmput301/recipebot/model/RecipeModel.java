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

package com.cmput301.recipebot.model;

import android.content.Context;
import com.cmput301.recipebot.client.ESClient;

import java.util.ArrayList;

public class RecipeModel {

    private DatabaseHelper dbHelper;
    private ESClient client;
    private static RecipeModel instance;

    private RecipeModel(Context context) {
        dbHelper = new DatabaseHelper(context);
        client = new ESClient();
    }

    /**
     * Get the instance. Attaches to application context regardless of context.
     *
     * @param context
     * @return
     */
    public static RecipeModel getInstance(Context context) {
        if (instance == null) {
            instance = new RecipeModel(context.getApplicationContext());
        }
        return instance;
    }

    public ArrayList<Recipe> loadRecipes() {
        return dbHelper.getAllRecipes();
    }

    public void insertRecipe(Recipe recipe) {
        dbHelper.insertRecipe(recipe);
    }

    public void deleteRecipe(String id) {
        dbHelper.deleteRecipe(id);
    }

    public Recipe getRecipe(String id) {
        return dbHelper.getRecipe(id);
    }

    public void updateRecipe(Recipe recipe) {
        dbHelper.updateRecipe(recipe);
    }

}

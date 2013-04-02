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
import android.os.AsyncTask;
import com.cmput301.recipebot.model.beans.Ingredient;
import com.cmput301.recipebot.model.beans.Recipe;
import com.cmput301.recipebot.model.local.DatabaseHelper;
import com.cmput301.recipebot.model.network.ESClient;
import com.cmput301.recipebot.util.NetworkUtils;

import java.util.ArrayList;

/**
 * This class communicates with the {@link DatabaseHelper} and {@link ESClient} asynchronously to post updates to it's receivers
 * UI -> Model -> Database/ESClient
 */
public class RecipeModel {

    /**
     * A static instance of the DatabaseHelper that the model talks to.
     */
    private static DatabaseHelper mDbHelper;

    /**
     * An instance of the ESClient object.
     */
    private ESClient mClient;

    /**
     * A static recipe instance that the UI talks.
     */
    private static RecipeModel instance;

    /**
     * Maintains a reference to the Application context. Used to check the network state.
     */
    private Context context;

    /**
     * A cache of last know recipes.
     */
    private static ArrayList<Recipe> mRecipes;

    /**
     * Aa list of views that are regstered for updates.
     */
    private ArrayList<RecipeView> views;

    /**
     * Get the instance. Attaches to application context regardless of context. If the instacne does not exist,
     * a new one is created.
     *
     * @param context Context to open the database.
     * @return A static instance of the model
     */
    public static RecipeModel getInstance(Context context) {
        if (instance == null) {
            instance = new RecipeModel(context.getApplicationContext());
        }
        return instance;
    }

    /**
     * Constructs the Model Object.
     *
     * @param context Contex tot open the database and check network state.
     */
    private RecipeModel(Context context) {
        this.context = context;
        mClient = new ESClient();
        views = new ArrayList<RecipeView>();
        mDbHelper = DatabaseHelper.getInstance(context);
    }

    /**
     * An interface that the views must implement to register for updates.
     */
    public interface RecipeView {
        /**
         * Called when the database is updated.
         *
         * @param recipes A list of updated ingredients.
         */
        public void update(ArrayList<Recipe> recipes);
    }

    /**
     * Add a view to the Model. This kicks of an AsyncTask to fetch the latest data.
     *
     * @param view The view to add.
     * @see #loadRecipes()
     */
    public void addView(RecipeView view) {
        views.add(view);
        loadRecipes();
    }

    /**
     * Unregister a view from updates to the model.
     *
     * @param view View to ungregister.
     */
    public void deleteView(RecipeView view) {
        views.remove(view);
    }

    /**
     * Notify all the views about a change in the model.
     */
    public void notifyViews() {
        for (RecipeView view : views) {
            view.update(mRecipes);
        }
    }

    /**
     * Load all recipes in the database. It returns a cached version of the last known data, and kicks of an
     * AsyncTask to fetch the latest data.
     *
     * @return Cached copy of the data.
     */
    public ArrayList<Recipe> loadRecipes() {
        // Start an AsyncTask to load all items.
        new LoadRecipesTask().execute();
        // In the meantime, return the cached version.
        return mRecipes;
    }

    /**
     * Insert a recipe to the database and server.
     *
     * @param recipe Recipe to insert.
     */
    public void insertRecipe(Recipe recipe) {
        new InsertRecipeTask().execute(recipe);
    }

    /**
     * Delete a recipe locally.
     *
     * @param id Id of the recipe to delete.
     */
    public void deleteRecipe(String id) {
        new DeleteRecipeTask().execute(id);
    }

    /**
     * Delete a recipe from the network.
     *
     * @param id ID of the network to delete the recipe.
     */
    public void networkDeleteRecipe(String id) {
        new NetworkDeleteRecipeTask().execute(id);
    }

    /**
     * Update a recipe. Updates recipe in the database and the network.
     *
     * @param recipe Recipe to update
     */
    public void updateRecipe(Recipe recipe) {
        new UpdateRecipeTask().execute(recipe);
    }

    /**
     * Search for recipes locally.
     *
     * @param name Name to search.
     * @return List of recipes matching the search.
     */
    public ArrayList<Recipe> searchRecipes(String name) {
        ArrayList<Recipe> recipes = new ArrayList<Recipe>();
        for (Recipe recipe : mRecipes) {
            if (recipe.getName().compareToIgnoreCase(name) == 0) {
                recipes.add(recipe);
            }
        }
        return recipes;
    }

    /**
     * Search for recipes by tag locally.
     *
     * @param tag Tag to search for.
     * @return list of recipes for this search.
     */
    public ArrayList<Recipe> searchRecipesFromTag(String tag) {
        ArrayList<Recipe> recipes = new ArrayList<Recipe>();
        for (Recipe recipe : mRecipes) {
            if (recipe.getTags() == null) {
                continue;
            }
            if (recipe.getTags().contains(tag)) {
                recipes.add(recipe);
            }
        }
        return recipes;
    }

    /**
     * Search for recipes by user locally.
     *
     * @param email email of the user to search for.
     * @return recipes for this search.
     */
    public ArrayList<Recipe> searchRecipesFromUser(String email) {
        ArrayList<Recipe> recipes = new ArrayList<Recipe>();
        for (Recipe recipe : mRecipes) {
            if (recipe.getUser().getEmail().compareToIgnoreCase(email) == 0) {
                recipes.add(recipe);
            }
        }
        return recipes;
    }

    /**
     * Search for recipes by a list of ingredients.
     *
     * @param searchIngredients Ingredients to search for.
     * @return recipes for this search.
     */
    public ArrayList<Recipe> searchRecipesByIngredients(ArrayList<Ingredient> searchIngredients) {
        ArrayList<Recipe> recipes = new ArrayList<Recipe>();
        for (Recipe recipe : mRecipes) {
            if (recipe.getIngredients().containsAll(searchIngredients)) {
                recipes.add(recipe);
            }
        }
        return recipes;
    }

    /**
     * A concrete implementation of the {@link UpdateRecipeViewTask}
     */
    private class LoadRecipesTask extends UpdateRecipeViewTask<Void> {
        @Override
        protected ArrayList<Recipe> doInBackground(Void... params) {
            return super.doInBackground(params);
        }
    }

    /**
     * A task that inserts a recipe into the database and on the network.
     */
    private class InsertRecipeTask extends UpdateRecipeViewTask<Recipe> {
        @Override
        protected ArrayList<Recipe> doInBackground(Recipe... params) {
            Recipe recipe = params[0];
            mDbHelper.insertRecipe(recipe);
            if (NetworkUtils.isConnected(context)) {
                mClient.insertRecipe(recipe);
            }
            return super.doInBackground(params);
        }
    }

    /**
     * A task that updates a recipe into the database and on the network.
     */
    private class UpdateRecipeTask extends UpdateRecipeViewTask<Recipe> {
        @Override
        protected ArrayList<Recipe> doInBackground(Recipe... params) {
            Recipe recipe = params[0];
            mDbHelper.updateRecipe(recipe);
            if (NetworkUtils.isConnected(context)) {
                mClient.updateRecipe(recipe);
            }
            return super.doInBackground(params);
        }
    }

    /**
     * A task that deletes a recipe from the database.
     */
    private class DeleteRecipeTask extends UpdateRecipeViewTask<String> {
        @Override
        protected ArrayList<Recipe> doInBackground(String... params) {
            String id = params[0];
            mDbHelper.deleteRecipe(id);
            return super.doInBackground(params);
        }
    }

    /**
     * A task that deletes a recipe from the network.
     */
    private class NetworkDeleteRecipeTask extends UpdateRecipeViewTask<String> {
        @Override
        protected ArrayList<Recipe> doInBackground(String... params) {
            String id = params[0];
            if (NetworkUtils.isConnected(context)) {
                mClient.deleteRecipe(id);
            }
            return super.doInBackground(params);
        }
    }

    /**
     * An abstract class that updates {@link #mRecipes} notifies it's views when that model is changed.
     */
    private abstract class UpdateRecipeViewTask<T> extends AsyncTask<T, Void, ArrayList<Recipe>> {
        @Override
        protected ArrayList<Recipe> doInBackground(T... params) {
            return mDbHelper.getAllRecipes();
        }

        @Override
        protected void onPostExecute(ArrayList<Recipe> recipes) {
            mRecipes = recipes;
            notifyViews();
            super.onPostExecute(recipes);
        }
    }

}

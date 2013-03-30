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
import com.cmput301.recipebot.model.local.DatabaseHelper;
import com.cmput301.recipebot.model.network.ESClient;
import com.cmput301.recipebot.util.NetworkUtils;

import java.util.ArrayList;

public class RecipeModel {

    private DatabaseHelper mDbHelper;
    private ESClient mClient;
    private static RecipeModel instance;
    private Context context;

    // A cache of last know recipes.
    private static ArrayList<Recipe> mRecipes;
    private ArrayList<RecipeView> views;

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

    private RecipeModel(Context context) {
        this.context = context;
        mClient = new ESClient();
        views = new ArrayList<RecipeView>();
        mDbHelper = DatabaseHelper.getInstance(context);
    }

    public interface RecipeView {
        public void update(ArrayList<Recipe> recipes);
    }

    public void addView(RecipeView view) {
        views.add(view);
        loadRecipes();
    }

    public void deleteView(RecipeView view) {
        views.remove(view);
    }

    public void notifyViews() {
        for (RecipeView view : views) {
            view.update(mRecipes);
        }
    }

    public ArrayList<Recipe> loadRecipes() {
        // Start an AsyncTask to load all items.
        new LoadRecipesTask().execute();
        // In the meantime, return the cached version.
        return mRecipes;
    }

    public void insertRecipe(Recipe recipe) {
        new InsertRecipeTask().execute(recipe);
    }

    public void deleteRecipe(String id) {
        new DeleteRecipeTask().execute(id);
    }

    public void networkDeleteRecipe(String id) {
        new NetworkDeleteRecipeTask().execute(id);
    }

    public void updateRecipe(Recipe recipe) {
        new UpdateRecipeTask().execute(recipe);
    }

    public ArrayList<Recipe> searchRecipes(String name) {
        ArrayList<Recipe> recipes = new ArrayList<Recipe>();
        for (Recipe recipe : mRecipes) {
            if (recipe.getName().compareToIgnoreCase(name) == 0) {
                recipes.add(recipe);
            }
        }
        return recipes;
    }

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

    public ArrayList<Recipe> searchRecipesFromUser(String email) {
        ArrayList<Recipe> recipes = new ArrayList<Recipe>();
        for (Recipe recipe : mRecipes) {
            if (recipe.getUser().getEmail().compareToIgnoreCase(email) == 0) {
                recipes.add(recipe);
            }
        }
        return recipes;
    }

    private class LoadRecipesTask extends UpdateRecipeViewTask<Void> {
        @Override
        protected ArrayList<Recipe> doInBackground(Void... params) {
            return super.doInBackground(params);
        }
    }

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

    private class DeleteRecipeTask extends UpdateRecipeViewTask<String> {
        @Override
        protected ArrayList<Recipe> doInBackground(String... params) {
            String id = params[0];
            mDbHelper.deleteRecipe(id);
            return super.doInBackground(params);
        }
    }

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

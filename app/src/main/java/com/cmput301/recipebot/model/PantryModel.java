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
import com.cmput301.recipebot.model.local.DatabaseHelper;

import java.util.ArrayList;

import static com.cmput301.recipebot.util.LogUtils.makeLogTag;

/**
 * This class communicates with the {@link DatabaseHelper} asynchronousy to post updates to it's receivers
 * UI -> Model -> Database.
 */
public class PantryModel {

    private static final String LOGTAG = makeLogTag(PantryModel.class);

    /**
     * A static instance of the DatabaseHelper that the model talks to.
     */
    private static DatabaseHelper dbHelper;

    /**
     * A static instance of the this model that the UI talks to.
     */
    private static PantryModel instance;

    /**
     * A cache of last known Pantry items that can be provided to the receiver.
     */
    private static ArrayList<Ingredient> mPantry;

    /**
     * A list of {@link PantryView} that are subscribed for updates.
     */
    private ArrayList<PantryView> views;

    /**
     * Constructs the model
     *
     * @param context Context to open the database.
     */
    private PantryModel(Context context) {
        mPantry = new ArrayList<Ingredient>();
        views = new ArrayList<PantryView>();
        dbHelper = DatabaseHelper.getInstance(context);
    }

    /**
     * An interface for views that want to register for updates to hte Pantry.
     */
    public interface PantryView {
        /**
         * Called when the pantry is updated.
         *
         * @param ingredientList A list of updated ingredients.
         */
        public void update(ArrayList<Ingredient> ingredientList);
    }

    /**
     * Add a view to the Model. This kicks of an AsyncTask to fetch the latest data.
     *
     * @param view The view to add.
     * @see #loadPantry()
     */
    public void addView(PantryView view) {
        views.add(view);
        loadPantry();
    }

    /**
     * Unregister a view from updates to the model.
     *
     * @param view View to ungregister.
     */
    public void deleteView(PantryView view) {
        views.remove(view);
    }

    /**
     * Notify all the views about a change in the model.
     */
    public void notifyViews() {
        for (PantryView view : views) {
            view.update(mPantry);
        }
    }

    /**
     * Get the instance of the model. Attaches to application context regardless of context.
     *
     * @param context Context to open the database.
     * @return A static instance of this model.
     */
    public static PantryModel getInstance(Context context) {
        if (instance == null) {
            instance = new PantryModel(context.getApplicationContext());
        }
        return instance;
    }

    /**
     * Load all ingredients in the pantry. It returns a cached version of the last known data, and kicks of an
     * AsyncTask to fetch the latest data.
     *
     * @return Cached copy of the data.
     */
    public ArrayList<Ingredient> loadPantry() {
        // Start an AsyncTask to load all items.
        new LoadPantryTask().execute();
        // In the meantime, return the cached version.
        return mPantry;
    }

    /**
     * insert an item into the Pantry (asynchronous)
     *
     * @param ingredient Ingredient to insert.
     */
    public void insertPantryItem(final Ingredient ingredient) {
        new InsertPantryItemTask().execute(ingredient);
    }

    /**
     * Delete an ingredient from the Pantry (asynchronous)
     *
     * @param id Name of the ingredient to delete.
     */
    public void deletePantryItem(String id) {
        new DeletePantryItemTask().execute(id);
    }

    /**
     * A task that insert an item to the pantry. It accepts an Ingredient object in its parameters.
     */
    private class InsertPantryItemTask extends UpdatePantryViewTask<Ingredient> {
        @Override
        protected ArrayList<Ingredient> doInBackground(Ingredient... params) {
            Ingredient ingredient = params[0];
            dbHelper.insertPantryItem(ingredient);
            return super.doInBackground(params);
        }
    }

    /**
     * A task that deletes an Ingredient from the Pantry. It accepts a name of the Ingredient object.
     */
    private class DeletePantryItemTask extends UpdatePantryViewTask<String> {
        @Override
        protected ArrayList<Ingredient> doInBackground(String... params) {
            String id = params[0];
            dbHelper.deletePantryItem(id);
            return super.doInBackground(params);
        }
    }

    /**
     * A concrete implementation of {@link UpdatePantryViewTask}
     */
    private class LoadPantryTask extends UpdatePantryViewTask<Void> {
        @Override
        protected ArrayList<Ingredient> doInBackground(Void... params) {
            return super.doInBackground(params);
        }
    }

    /**
     * An abstract task that updates the {@link #mPantry} and notifies the vhild views.
     * All update tasks should be derived from this.
     *
     * @param <T>
     */
    private abstract class UpdatePantryViewTask<T> extends AsyncTask<T, Void, ArrayList<Ingredient>> {
        @Override
        protected ArrayList<Ingredient> doInBackground(T... params) {
            return dbHelper.getAllPantryItems();
        }

        @Override
        protected void onPostExecute(ArrayList<Ingredient> ingredients) {
            mPantry = ingredients;
            notifyViews();
            super.onPostExecute(ingredients);
        }
    }

}

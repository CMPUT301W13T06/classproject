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

package com.cmput301.recipebot.model.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.cmput301.recipebot.model.beans.Ingredient;
import com.cmput301.recipebot.model.beans.Recipe;
import com.google.gson.Gson;

import java.util.ArrayList;

import static com.cmput301.recipebot.util.LogUtils.makeLogTag;

/**
 * A wrapper around {@link SQLiteOpenHelper} and {@link SQLiteDatabase} so we can just query for our
 * objects. It maintians a static instance of itself that should be used by other objects that need this.
 *
 * @see #instance
 * @see #getInstance(android.content.Context)
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String LOGTAG = makeLogTag(DatabaseHelper.class);

    /**
     * The database name.
     */
    private static final String DATABASE_NAME = "recipebot.db";

    /**
     * The database version.
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * The table name for storing recipes.
     */
    private static final String TABLE_RECIPES = "recipes";
    private static final String COLUMN_RECIPE_ID = "_id";
    private static final String COLUMN_RECIPE_DATA = "data";

    /**
     * Create statement for {@link #TABLE_RECIPES}
     */
    private static final String CREATE_TABLE_RECIPES = "create table "
            + TABLE_RECIPES + "(" + COLUMN_RECIPE_ID
            + " text primary key, " + COLUMN_RECIPE_DATA
            + " text not null);";

    /**
     * The table name for storing pantry objects {@link Ingredient}.
     */
    private static final String TABLE_PANTRY = "pantry";
    private static final String COLUMN_PANTRY_ID = "_id";
    private static final String COLUMN_PANTRY_DATA = "data";

    /**
     * Create statement for {@link #TABLE_PANTRY}
     */
    private static final String CREATE_TABLE_PANTRY = "create table "
            + TABLE_PANTRY + "(" + COLUMN_PANTRY_ID
            + " text primary key, " + COLUMN_PANTRY_DATA
            + " text not null);";

    /**
     * A {@link Gson} instance to serialize and de-serialize our data.
     */
    private Gson mGson;

    /**
     * A static instance of this class.
     */
    private static DatabaseHelper instance;

    /**
     * Constructs the Database Helper.
     *
     * @param context Context to open the database.
     */
    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mGson = new Gson();
    }

    /**
     * Returns a static instance of the DatabaseHelper. If the instance does not exist, a new one is created.
     *
     * @param context Context to open the database. Regardless of the Context provided, it uses the Application context.
     * @return an instance of the DatabaseHelper.
     */
    public static DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_RECIPES);
        sqLiteDatabase.execSQL(CREATE_TABLE_PANTRY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PANTRY);
        onCreate(db);
    }

    /**
     * Insert a recipe into the database.
     *
     * @param recipe Recipe to insert.
     */
    public void insertRecipe(Recipe recipe) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_RECIPE_ID, recipe.getId());
        values.put(COLUMN_RECIPE_DATA, mGson.toJson(recipe));
        db.insert(TABLE_RECIPES, null, values);
    }

    /**
     * Get a recipe from the database.
     *
     * @param id ID of recipe we want.
     */
    public Recipe getRecipe(String id) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(TABLE_RECIPES, null, COLUMN_RECIPE_ID + "=?",
                new String[]{id}, null, null, null, null);

        cursor.moveToFirst();
        int index = cursor.getColumnIndexOrThrow(COLUMN_RECIPE_DATA);
        String json = cursor.getString(index);
        Recipe recipe = mGson.fromJson(json, Recipe.class);

        cursor.close();

        return recipe;
    }

    /**
     * Get all recipes from the database.
     *
     * @return All recipes in the database.
     */
    public ArrayList<Recipe> getAllRecipes() {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(TABLE_RECIPES, null, null,
                null, null, null, null, null);

        if (cursor == null) {
            return null;
        }
        cursor.moveToFirst();

        ArrayList<Recipe> recipes = new ArrayList<Recipe>();

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int index = cursor.getColumnIndexOrThrow(COLUMN_RECIPE_DATA);
            String json = cursor.getString(index);
            Recipe recipe = mGson.fromJson(json, Recipe.class);
            recipes.add(recipe);
            cursor.moveToNext();
        }

        cursor.close();

        return recipes;
    }

    /**
     * Delete a recipe from the database.
     *
     * @param id ID of the recipe to delete.
     */
    public void deleteRecipe(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_RECIPES, COLUMN_RECIPE_ID + "=?", new String[]{id});
    }

    /**
     * Update a recipe in the database.
     *
     * @param recipe Recipe to update.
     */
    public void updateRecipe(Recipe recipe) {
        deleteRecipe(recipe.getId());
        insertRecipe(recipe);
    }

    /**
     * Get a list of all items in our pantry.
     *
     * @return All Ingredient in the Pantry
     */
    public ArrayList<Ingredient> getAllPantryItems() {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(TABLE_PANTRY, null, null,
                null, null, null, null, null);

        if (cursor == null) {
            return null;
        }
        cursor.moveToFirst();

        ArrayList<Ingredient> pantry = new ArrayList<Ingredient>();

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int index = cursor.getColumnIndexOrThrow(COLUMN_PANTRY_DATA);
            String json = cursor.getString(index);
            Ingredient item = mGson.fromJson(json, Ingredient.class);
            pantry.add(item);
            cursor.moveToNext();
        }

        cursor.close();

        return pantry;

    }

    /**
     * Insert an item into the pantry.
     */
    public void insertPantryItem(Ingredient ingredient) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PANTRY_ID, ingredient.getName());
        values.put(COLUMN_PANTRY_DATA, mGson.toJson(ingredient));
        db.insert(TABLE_PANTRY, null, values);
    }

    /**
     * Delete a recipe from the database.
     *
     * @param name name of the ingredient to delete.
     */
    public void deletePantryItem(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PANTRY, COLUMN_PANTRY_ID + "=?", new String[]{name});
    }

}

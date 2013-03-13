package com.cmput301.recipebot.model;
//code from: http://www.youtube.com/watch?v=v61A90qlK9s

/*
* Method Summary:
*
*   public void addRecipe(int id, String writer, String recipe_name, String prep, ArrayList<String> ingr, ArrayList<String> pics) {
        SQLiteDatabase db = this.getWritableDatabase();
    public ArrayList<String> loadPantry();
    public ArrayList<Recipe> loadRecipes();
    public void editRecipeName(Recipe recipe, String r_name);
    public void addPantryItem(String food_name);
    public void removePantryItem(String food_name);
* */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;

public class DbHelper extends SQLiteOpenHelper {

    //Define Database table + column names
    private static final String TAG = "DbHelper";
    public static final String DB_NAME = "log.db";
    public static final int DB_VERSION = 21;

    public static final String INGREDIENTS_TABLE = "ingredients";
    public static final String I_ID = BaseColumns._ID;
    public static final String REC_ID = "rec_id";
    public static final String INGREDIENT = "ingredient";

    public static final String FOODS_TABLE = "foods";
    public static final String F_NAME = "f_name";

    public static final String RECIPES_TABLE = "recepies";
    public static final String R_ID = BaseColumns._ID;
    public static final String R_NAME = "r_name";
    public static final String WRITER = "writer";
    public static final String PREP = "prep";

    public static final String HASIMAGES_TABLE = "has_images";
    public static final String HIT_ID = BaseColumns._ID;
    public static final String RECIPE = "r_id";
    public static final String IMAGE_PATH = "image_path";

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //FOODS_TABLE
        String sql1 = String.format("create table %s (%s TEXT primary key)",
                FOODS_TABLE, F_NAME);
        Log.d(TAG, "creating FOODS_TABLE");
        //RECIPES_TABLE
        String sql2 = String.format("create table %s (%s INTEGER primary key, %s TEXT, %s TEXT, " +
                "%s TEXT)",
                RECIPES_TABLE, R_ID, WRITER, R_NAME, PREP);

        //HASIMAGES_TABLE
        String sql3 = String.format("create table %s (%s INTEGER primary key, %s INTEGER, %s TEXT, " +
                "foreign key(%s) references %s)",
                HASIMAGES_TABLE, HIT_ID, RECIPE, IMAGE_PATH, RECIPE, RECIPES_TABLE);

        //INGREDIENTS_TABLE
        String sql4 = String.format("create table %s (%s INTEGER primary key, %s INTEGER, %s TEXT, " +
                "foreign key(%s) references %s)",
                INGREDIENTS_TABLE, I_ID, REC_ID, INGREDIENT, REC_ID, RECIPES_TABLE);

        //Create tables
        try {
            db.execSQL(sql1);
        } catch (Exception e) {
            Log.e(TAG, "FAILED TO CREATE TABLE: " + FOODS_TABLE);
        }
        try {
            db.execSQL(sql2);
        } catch (Exception e) {
            Log.e(TAG, "FAILED TO CREATE TABLE: " + RECIPES_TABLE);
        }
        try {
            db.execSQL(sql3);
        } catch (Exception e) {
            Log.e(TAG, "FAILED TO CREATE TABLE: " + HASIMAGES_TABLE);
        }
        try {
            db.execSQL(sql4);
        } catch (Exception e) {
            Log.e(TAG, "FAILED TO CREATE TABLE: " + INGREDIENTS_TABLE);
        }
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL("drop table if exists " + FOODS_TABLE);
            db.execSQL("drop table if exists " + RECIPES_TABLE);
            db.execSQL("drop table if exists " + HASIMAGES_TABLE);
            db.execSQL("drop table if exists " + INGREDIENTS_TABLE);
        } catch (Exception e) {
            Log.d("dbUPGRADE", "failed to drop all tables");
        }

        Log.d(TAG, "onUpdate dropped table ALL");
        this.onCreate(db);
    }
    // code from: http://www.androidhive.info/2011/11/android-sqlite-database-tutorial/
    //returns a list of log entries

    /*
    * Function returns a list of ingredients
    * from the user's pantry
    * */
    public ArrayList<String> loadPantry() {

        ArrayList<String> logList = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + FOODS_TABLE, null);
        if (cursor.moveToFirst()) {
            try {
                do {
                    logList.add(cursor.getString(0));
                } while (cursor.moveToNext());
            } catch (Exception e) {
                Log.e(TAG, "failed to load all Foods from: " + FOODS_TABLE);
            }
        }
        cursor.close();
        db.close();
        return logList;
    }

    /*
    * Returns all the Recipes that are stored
    * in the database of a user
    * */
    public ArrayList<Recipe> loadRecipes() {

        ArrayList<Recipe> logList = new ArrayList<Recipe>();
        ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
        ArrayList<String> photos = new ArrayList<String>();
        ArrayList<String> directions = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + RECIPES_TABLE + " ORDER BY " + R_NAME + " ASC", null);
        if (cursor.moveToFirst()) {
            try {
                do {
                    //SET RECIPE INFO
                    Recipe recipe = new Recipe();
                    recipe.setId(cursor.getInt(0));
                    recipe.setUser(cursor.getString(1));
                    recipe.setName(cursor.getString(2));
                    directions.add(cursor.getString(3));
                    recipe.setDirections(directions);

                    //SET INGREDIENTS
                    Cursor cursor2 = db.rawQuery("SELECT " + INGREDIENT + " FROM " + INGREDIENTS_TABLE + " WHERE " +
                            REC_ID + " =" + cursor.getInt(0), null);
                    if (cursor2.moveToFirst()) {
                        try {
                            do {
                                Ingredient ingredient = new Ingredient(cursor2.getString(0), "", 0);
                                ingredients.add(ingredient);
                            } while (cursor2.moveToNext());
                        } catch (Exception e) {
                            Log.e(TAG, "failed to load INGREDIENTS from: " + INGREDIENTS_TABLE);
                        }
                    }
                    cursor2.close();
                    recipe.setIngredients(ingredients);

                    //SET PHOTOS
                    Cursor cursor3 = db.rawQuery("SELECT " + IMAGE_PATH + " FROM " + HASIMAGES_TABLE + " WHERE " +
                            RECIPE + " =" + cursor.getInt(0), null);
                    if (cursor3.moveToFirst()) {
                        try {
                            do {
                                photos.add(cursor3.getString(0));
                            } while (cursor3.moveToNext());
                        } catch (Exception e) {
                            Log.e(TAG, "failed to load IMAGES from: " + HASIMAGES_TABLE);
                        }
                    }
                    cursor3.close();
                    recipe.setPhotos(photos);

                    logList.add(recipe);
                } while (cursor.moveToNext());
            } catch (Exception e) {
                Log.e(TAG, "failed to load all Recipes from: " + RECIPES_TABLE);
            }
        }
        cursor.close();
        db.close();
        return logList;
    }

    /*
    * Adds a recipe to the database
    * */
    public void addRecipe(int id, String writer, String recipe_name, String prep, ArrayList<String> ingr, ArrayList<String> pics) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(WRITER, writer);
        values.put(R_NAME, recipe_name);
        values.put(PREP, prep);
        values.put(R_ID, id);

        // Inserting Row
        try {
            db.insertOrThrow(RECIPES_TABLE, null, values);
        } catch (Exception e) {
            Log.e(TAG, "Couldn't put entry into: " + RECIPES_TABLE);
        }

        ContentValues values2 = new ContentValues();
        //Insert all the ingredients of the recipe and give it a common id
        for (int x = 0; x < ingr.size(); x++) {
            values2.put(INGREDIENT, ingr.get(x));
            values2.put(REC_ID, id);

            try {
                db.insertOrThrow(INGREDIENTS_TABLE, null, values2);
                Log.d(TAG, "try: add ingredient SUCCESS: " + ingr.get(x));
            } catch (Exception e) {
                Log.e(TAG, "Couldn't put entry into: " + INGREDIENTS_TABLE + ": " + ingr.get(x));
            }
            values2.clear();
        }

        ContentValues values3 = new ContentValues();
        //Insert all the pictures of the recipe and give it a common id
        for (int y = 0; y < pics.size(); y++) {
            values3.put(IMAGE_PATH, pics.get(y));
            values3.put(RECIPE, id);

            try {
                db.insertOrThrow(HASIMAGES_TABLE, null, values3);
                Log.d(TAG, "try: add photo SUCCESS: " + pics.get(y));
            } catch (Exception e) {
                Log.e(TAG, "Couldn't put entry into: " + HASIMAGES_TABLE + ": " + pics.get(y));
            }
            values3.clear();
        }
        values.clear();
        db.close();
    }
    /*
    * Edit complex edits different parts of the recipe object
    * changing the corresponding data in the database as well
    * as in the passed Recipe object. For Images and Ingredients
    * upon edit calls, the database deletes any old images or ingredients
    * and then adds the new ones that are passed to the function
    * (it is done so for the simplicity of implementation)
    * */

    //Edits the Recipe Name
    public void editRecipeName(Recipe recipe, String r_name) {
        SQLiteDatabase db = this.getWritableDatabase();

        String sql1 = String.format("UPDATE " + RECIPES_TABLE + " SET " + R_NAME + " ='%s'" +
                " WHERE " + R_ID + " =%s", r_name, recipe.getId());
        try {
            db.execSQL(sql1);
            recipe.setName(r_name);
        } catch (Exception e) {
            Log.e(TAG, "FAILED TO UPDATE A RECIPE: " + recipe.getName());
        }

        db.close();
    }

    //Edits the Description/Preparation of a recipe
    public void editRecipePrep(Recipe recipe, String prep) {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<String> preparation = new ArrayList<String>();
        preparation.add(prep);

        //UPDATE RECIPE_TABLE
        String sql1 = String.format("UPDATE " + RECIPES_TABLE + " SET " + PREP + " ='%s'" +
                " WHERE " + R_ID + " =%s", prep, recipe.getId());
        try {
            db.execSQL(sql1);
            recipe.setDirections(preparation);
        } catch (Exception e) {
            Log.e(TAG, "FAILED TO UPDATE A RECIPE: " + prep);
        }

        db.close();
    }

    //Edits the recipe's ingredients removing old ones first and adding the new ones
    public void editRecipeIngredients(Recipe recipe, ArrayList<String> ingredients) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues(0);
        ArrayList<Ingredient> ingr = new ArrayList<Ingredient>();

        db.delete(INGREDIENTS_TABLE, REC_ID + " = ?", new String[]{String.valueOf(recipe.getId())});

        for (int x = 0; x < ingredients.size(); x++) {

            values.put(REC_ID, recipe.getId());
            values.put(INGREDIENT, ingredients.get(x));
            Ingredient ingredient = new Ingredient(ingredients.get(x), "", 0);
            ingr.add(ingredient);
            try {
                db.insertOrThrow(INGREDIENTS_TABLE, null, values);
            } catch (Exception e) {
                Log.e(TAG, "FAILED TO UPDATE INGREDIENT: " + ingredients.get(x));
            }
        }
        recipe.setIngredients(ingr);
        db.close();
    }

    //edits the recipe's pictures
    public void editRecipePictures(Recipe recipe, ArrayList<String> pictures) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues(0);
        db.delete(HASIMAGES_TABLE, RECIPE + " = ?", new String[]{String.valueOf(recipe.getId())});

        for (int x = 0; x < pictures.size(); x++) {

            values.put(RECIPE, recipe.getId());
            values.put(IMAGE_PATH, pictures.get(x));
            try {
                db.insertOrThrow(HASIMAGES_TABLE, null, values);
                recipe.setPhotos(pictures);
            } catch (Exception e) {
                Log.e(TAG, "FAILED TO UPDATE A PICTURE: " + pictures.get(x));
            }
        }

        db.close();
    }

    /*
    *  Given the recipe id, it removes the recipe and all
    *  corresponding pictures and ingredients
    * */
    public void removeRecipe(int r_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Log.d(TAG, "removing recipe and depenencies");
            db.delete(RECIPES_TABLE, R_ID + " = ?", new String[]{String.valueOf(r_id)});
            db.delete(INGREDIENTS_TABLE, REC_ID + " = ?", new String[]{String.valueOf(r_id)});
            db.delete(HASIMAGES_TABLE, RECIPE + " = ?", new String[]{String.valueOf(r_id)});
        } catch (Exception e) {
            Log.e(TAG, "Couldn't remove a recipe and all db sub trees");
        }
        db.close();
    }

    //Removes a pantry item called food_name
    public void removePantryItem(String food_name) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.delete(FOODS_TABLE, F_NAME + " = ?", new String[]{String.valueOf(food_name)});
        } catch (Exception e) {
            Log.e(TAG, "FAILED TO REMOVE FROM PANTRY: " + food_name);
        }
        db.close();
    }

    //adds a new item to the user's pantry
    public void addPantryItem(String food_name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put(F_NAME, food_name);
        try {
            db.insertOrThrow(FOODS_TABLE, null, value);
        } catch (Exception e) {
            Log.d(TAG, "FAILED TO ADD A PANTRY ITEM: " + food_name + "(already exists)");
        }
        db.close();
    }
}
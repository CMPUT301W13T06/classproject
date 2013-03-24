package com.cmput301.recipebot.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.google.gson.Gson;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "recipebot.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_RECIPES = "recipes";
    public static final String COLUMN_RECIPE_ID = "_id";
    public static final String COLUMN_RECIPE_DATA = "data";

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_RECIPES + "(" + COLUMN_RECIPE_ID
            + " text primary key, " + COLUMN_RECIPE_DATA
            + " text not null);";

    Gson mGson;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mGson = new Gson();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPES);
        onCreate(db);
    }

    public void insertRecipe(Recipe recipe) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_RECIPE_ID, recipe.getId());
        values.put(COLUMN_RECIPE_DATA, mGson.toJson(recipe));
        db.insert(TABLE_RECIPES, null, values);
        db.close();
    }

    public Recipe getRecipe(String id) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(TABLE_RECIPES, null, COLUMN_RECIPE_ID + "=?",
                new String[]{id}, null, null, null, null);

        if (cursor == null) {
            return null;
        }
        cursor.moveToFirst();
        int index = cursor.getColumnIndexOrThrow(COLUMN_RECIPE_DATA);
        String json = cursor.getString(index);
        Recipe recipe = mGson.fromJson(json, Recipe.class);

        db.close();
        cursor.close();

        return recipe;
    }
}

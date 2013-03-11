/*
 * Copyright 2013 Adam Saturna
 * Copyright 2013 Brian Trinh
 * Copyright 2013 Ethan Mykytiuk
 * Copyright 2013 Prateek Srivastava (@f2prateek)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cmput301.recipebot.ui.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.cmput301.recipebot.R;
import com.cmput301.recipebot.model.Recipe;
import com.cmput301.recipebot.ui.adapters.RecipeGridAdapter;
import com.github.rtyley.android.sherlock.roboguice.fragment.RoboSherlockFragment;

import java.util.ArrayList;

/**
 * A simple fragment that shows a list of recipes
 */
public class SavedRecipesFragment extends RoboSherlockFragment implements AdapterView.OnItemClickListener {

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_saved_recipes, container, false);
        GridView gridview = (GridView) v.findViewById(R.id.gridview);
        gridview.setAdapter(new RecipeGridAdapter(getSherlockActivity(), getTestRecipes()));
        gridview.setOnItemClickListener(this);
        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_saved_recipes, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_search_saved_recipes:
                // TODO : search recipes
                Toast.makeText(getSherlockActivity(), "TODO: Search Recipe", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getSherlockActivity(), "clicked recipe # " + position, Toast.LENGTH_SHORT).show();
    }

    private ArrayList<Recipe> getTestRecipes() {

        // TODO : this is just a test, should also do on a background thread, check for recycled views etc.
        int[] bitmapIDs = {R.drawable.test_recipe_image_1, R.drawable.test_recipe_image_2,
                R.drawable.test_recipe_image_3, R.drawable.test_recipe_image_4, R.drawable.test_recipe_image_5,
                R.drawable.test_recipe_image_6};

        ArrayList<Recipe> recipes = new ArrayList<Recipe>();
        for (int i = 0; i < 15; i++) {
            Bitmap test = BitmapFactory.decodeResource(getSherlockActivity().getResources(),
                    bitmapIDs[i % bitmapIDs.length]);
            ArrayList<Bitmap> bitmaps = new ArrayList<Bitmap>();
            bitmaps.add(test);
            Recipe r = new Recipe.Builder().setDirections(null).setId(0).setIngredients(null).setName("hi").
                    setUser("bob").setImages(bitmaps).build();
            recipes.add(r);
        }

        return recipes;
    }
}
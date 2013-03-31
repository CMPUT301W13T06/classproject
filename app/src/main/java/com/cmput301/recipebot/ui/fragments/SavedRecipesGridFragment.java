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

package com.cmput301.recipebot.ui.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import com.cmput301.recipebot.R;
import com.cmput301.recipebot.model.RecipeModel;
import com.cmput301.recipebot.model.beans.Recipe;
import com.cmput301.recipebot.model.beans.User;
import com.cmput301.recipebot.ui.adapters.RecipeGridAdapter;
import com.cmput301.recipebot.util.AppConstants;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment that registers to recipe any database updates.
 */
public class SavedRecipesGridFragment extends AbstractRecipeGridFragment implements RecipeModel.RecipeView,
        AdapterView.OnItemLongClickListener {

    @Override
    public void onResume() {
        super.onResume();
        RecipeModel.getInstance(getSherlockActivity()).addView(this);
    }

    @Override
    public void onPause() {
        RecipeModel.getInstance(getSherlockActivity()).deleteView(this);
        super.onPause();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        List<Recipe> recipeList = RecipeModel.getInstance(getSherlockActivity()).loadRecipes();
        if (recipeList != null && recipeList.size() != 0) {
            initaliseGridView(recipeList);
        }
        gridview.setOnItemLongClickListener(this);
    }

    private void initaliseGridView(List<Recipe> recipes) {
        mAdapter = new RecipeGridAdapter(getSherlockActivity(), recipes);
        gridview.setAdapter(mAdapter);
    }

    @Override
    public void update(ArrayList<Recipe> recipes) {
        if (mAdapter == null) {
            initaliseGridView(recipes);
        } else {
            mAdapter.swapData(recipes);
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Recipe recipe = (Recipe) view.getTag();
        User user = getUserFromPreferences();
        if (recipe.getUser().getId() == user.getId()) {
            // The recipe is by this user, so delete it from the network
            RecipeModel.getInstance(getSherlockActivity()).networkDeleteRecipe(recipe.getId());
        }
        RecipeModel.getInstance(getSherlockActivity()).deleteRecipe(recipe.getId());
        Crouton.makeText(getSherlockActivity(), R.string.recipe_deleted, Style.ALERT).show();
        return true;
    }

    private User getUserFromPreferences() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getSherlockActivity());
        String email = sharedPref.getString(AppConstants.KEY_USER_EMAIL, null);
        String name = sharedPref.getString(AppConstants.KEY_USER_NAME, null);
        return new User(email, name);
    }
}

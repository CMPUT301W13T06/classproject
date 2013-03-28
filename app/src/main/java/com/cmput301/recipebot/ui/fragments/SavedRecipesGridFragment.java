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

import android.os.Bundle;
import android.view.View;
import com.cmput301.recipebot.model.Recipe;
import com.cmput301.recipebot.model.RecipeModel;
import com.cmput301.recipebot.ui.adapters.RecipeGridAdapter;

import java.util.ArrayList;
import java.util.List;

public class SavedRecipesGridFragment extends AbstractRecipeGridFragment implements RecipeModel.RecipeView {

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
            mAdapter = new RecipeGridAdapter(getSherlockActivity(), recipeList);
            gridview.setAdapter(mAdapter);
        }
    }

    @Override
    public void update(ArrayList<Recipe> recipes) {
        if (mAdapter == null) {
            mAdapter = new RecipeGridAdapter(getSherlockActivity(), recipes);
            gridview.setAdapter(mAdapter);
        } else {
            mAdapter.swapData(recipes);
        }
    }
}

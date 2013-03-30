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

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import com.cmput301.recipebot.R;
import com.cmput301.recipebot.model.beans.Recipe;
import com.cmput301.recipebot.ui.ViewRecipeActivity;
import com.cmput301.recipebot.ui.adapters.RecipeGridAdapter;
import com.github.rtyley.android.sherlock.roboguice.fragment.RoboSherlockFragment;
import roboguice.inject.InjectView;

import static com.cmput301.recipebot.util.LogUtils.makeLogTag;

/**
 * A simple fragment that shows a list of {@link Recipe} items.
 */
public abstract class AbstractRecipeGridFragment extends RoboSherlockFragment implements AdapterView.OnItemClickListener {

    private static final String LOGTAG = makeLogTag(AbstractRecipeGridFragment.class);
    protected static final String RECIPE_ARGS = "recipes";

    @InjectView(R.id.gridview)
    GridView gridview;

    protected RecipeGridAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.saved_recipes_grid, container, false);
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        gridview.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getSherlockActivity(), ViewRecipeActivity.class);
        intent.putExtra(ViewRecipeActivity.EXTRA_RECIPE, (Recipe) view.getTag());
        startActivity(intent);
    }
}
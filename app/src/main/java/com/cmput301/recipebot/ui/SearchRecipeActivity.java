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

package com.cmput301.recipebot.ui;

import android.os.Bundle;
import com.cmput301.recipebot.model.beans.Recipe;
import com.cmput301.recipebot.ui.fragments.NetworkRecipeGridFragment;
import roboguice.inject.InjectExtra;

import java.util.ArrayList;

import static com.cmput301.recipebot.util.LogUtils.makeLogTag;

/**
 * Contains a {@link NetworkRecipeGridFragment} to display recipes from the network
 */
public class SearchRecipeActivity extends BaseActivity {

    private static final String LOGTAG = makeLogTag(SearchRecipeActivity.class);

    public static final String EXTRA_RECIPE_LIST = "EXTRA_RECIPE_LIST";
    public static final String EXTRA_SEARCH_TERM = "EXTRA_SEARCH_TERM";

    @InjectExtra(EXTRA_RECIPE_LIST)
    ArrayList<Recipe> recipes;
    @InjectExtra(EXTRA_SEARCH_TERM)
    String query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setTitle(query);

        if (savedInstanceState == null) {
            NetworkRecipeGridFragment f = NetworkRecipeGridFragment.newInstance(recipes);
            getSupportFragmentManager().beginTransaction().add(android.R.id.content, f).commit();
        }

    }

}

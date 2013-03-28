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

package com.cmput301.recipebot.test;

import android.app.Instrumentation;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import com.cmput301.recipebot.ui.SearchRecipeActivity;
import com.squareup.spoon.Spoon;

import static org.fest.assertions.api.ANDROID.assertThat;

public class RecipeSearchActivityTest extends ActivityInstrumentationTestCase2<SearchRecipeActivity> {

    protected Instrumentation instrumentation;
    protected SearchRecipeActivity activity;

    public RecipeSearchActivityTest() {
        super(SearchRecipeActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        instrumentation = getInstrumentation();
        setActivityIntent(getTestIntent()); // Set intent first
        activity = getActivity();
    }

    /**
     * Verify that {@link com.cmput301.recipebot.ui.RecipeActivity} exists
     */
    public void testRecipeActivityExists() {
        assertThat(activity).isNotNull();
        Spoon.screenshot(activity, "initial_state");
    }

    private Intent getTestIntent() {
        Intent intent = new Intent(instrumentation.getContext(), SearchRecipeActivity.class);
        intent.putExtra(SearchRecipeActivity.EXTRA_RECIPE_LIST, "Roasted");
        return intent;
    }
}

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
import com.cmput301.recipebot.model.beans.Recipe;
import com.cmput301.recipebot.ui.EditRecipeActivity;
import com.cmput301.recipebot.ui.ViewRecipeActivity;
import com.squareup.spoon.Spoon;

import static com.cmput301.recipebot.util.TestDataSetGenerator.getTestRecipe;
import static org.fest.assertions.api.ANDROID.assertThat;

/**
 * Tests for a new Recipe Activity
 */
public class ViewRecipeActivityTest extends ActivityInstrumentationTestCase2<ViewRecipeActivity> {

    protected Instrumentation instrumentation;
    protected ViewRecipeActivity activity;
    private Recipe recipe;

    /**
     * Create test for {@link com.cmput301.recipebot.ui.EditRecipeActivity}
     */
    public ViewRecipeActivityTest() {
        super(ViewRecipeActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        instrumentation = getInstrumentation();
        recipe = getTestRecipe();
        setActivityIntent(makeTestIntent(recipe));
        activity = getActivity();
    }

    private Intent makeTestIntent(Recipe recipe) {
        Intent intent = new Intent(instrumentation.getContext(), EditRecipeActivity.class);
        intent.putExtra(EditRecipeActivity.EXTRA_RECIPE, recipe);
        return intent;
    }

    /**
     * Test that new recipe activity can be started.
     */
    public void testViewRecipeActivityExists() {
        assertThat(activity).isNotNull();
        Spoon.screenshot(activity, "initial_state");
    }
}

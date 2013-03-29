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
import android.support.v4.view.ViewPager;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;
import android.widget.LinearLayout;
import com.cmput301.recipebot.R;
import com.cmput301.recipebot.model.Recipe;
import com.cmput301.recipebot.ui.RecipeActivity;
import com.squareup.spoon.Spoon;

import static com.cmput301.recipebot.util.TestDataSetGenerator.getTestRecipe;
import static org.fest.assertions.api.ANDROID.assertThat;

public class RecipeActivityTest extends ActivityInstrumentationTestCase2<RecipeActivity> {

    protected Instrumentation instrumentation;

    /**
     * Create test for {@link com.cmput301.recipebot.ui.RecipeActivity}
     */
    public RecipeActivityTest() {
        super(RecipeActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        instrumentation = getInstrumentation();
    }

    private Intent makeTestIntent(Recipe recipe) {
        Intent intent = new Intent(instrumentation.getContext(), RecipeActivity.class);
        intent.putExtra(RecipeActivity.EXTRA_RECIPE, recipe);
        return intent;
    }

    /**
     * Verify that {@link RecipeActivity} exists.
     */
    public void testRecipeActivityIsShownCorrectly() {
        Recipe recipe = getTestRecipe();
        setActivityIntent(makeTestIntent(recipe));
        RecipeActivity activity = getActivity();
        assertThat(activity).isNotNull();
        Spoon.screenshot(activity, "initial_state");

        final EditText editTextName = (EditText) activity.findViewById(R.id.editText_recipe_name);
        assertThat(editTextName).hasTextString(recipe.getName());
        final EditText editTextDescription = (EditText) activity.findViewById(R.id.editText_recipe_description);
        assertThat(editTextDescription).hasTextString(recipe.getDescription());
        final LinearLayout ingredientList = (LinearLayout) activity.findViewById(R.id.list_ingredients);
        assertThat(ingredientList).hasChildCount(recipe.getIngredients().size());
        final LinearLayout directionsList = (LinearLayout) activity.findViewById(R.id.list_directions);
        assertThat(directionsList).hasChildCount(recipe.getDirections().size());
        final LinearLayout tagsList = (LinearLayout) activity.findViewById(R.id.list_tags);
        assertThat(tagsList).hasChildCount(recipe.getTags().size());

        final ViewPager viewPager = (ViewPager) activity.findViewById(R.id.pager_recipe_images);
        assertThat(viewPager.getAdapter()).hasCount(7);
        for (int i = 0; i < 7; i++) {
            final int count = i;
            instrumentation.runOnMainSync(new Runnable() {
                @Override
                public void run() {
                    viewPager.setCurrentItem(count);
                }
            });
            instrumentation.waitForIdleSync();
            Spoon.screenshot(activity, "pager_" + count);
        }
    }

    /**
     * Test that new recipe activity can be started.
     */
    public void testNewRecipeActivity() {
        RecipeActivity activity = getActivity();
        assertThat(activity).isNotNull();
        Spoon.screenshot(activity, "initial_state");
    }

}

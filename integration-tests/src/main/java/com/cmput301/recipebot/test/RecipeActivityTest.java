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
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import com.cmput301.recipebot.R;
import com.cmput301.recipebot.model.Recipe;
import com.cmput301.recipebot.ui.RecipeActivity;
import com.squareup.spoon.Spoon;

import static com.cmput301.recipebot.util.TestDataSetGenerator.generateRandomRecipe;
import static com.cmput301.recipebot.util.TestDataSetGenerator.getTestRecipe;
import static org.fest.assertions.api.ANDROID.assertThat;

/**
 * Test for existing recipe activity.
 */
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
    public void testActivityExists() throws Exception {
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
     * Verify that {@link RecipeActivity} is shown with empty descriptions.
     */
    public void testIsShownWithEmptyDescription() throws Exception {
        Recipe recipe = generateRandomRecipe();
        recipe.setDescription(null);
        setActivityIntent(makeTestIntent(recipe));
        RecipeActivity activity = getActivity();
        assertThat(activity).isNotNull();
        Spoon.screenshot(activity, "initial_state");
        final EditText editTextDescription = (EditText) activity.findViewById(R.id.editText_recipe_description);
        assertThat(editTextDescription).hasTextString("");
    }

    /**
     * Verify that {@link RecipeActivity} is shown with empty descriptions.
     */
    public void testIsShownWithEmptyDirectionsAndSaveFails() throws Exception {
        Recipe recipe = generateRandomRecipe();
        recipe.setDirections(null);
        setActivityIntent(makeTestIntent(recipe));
        RecipeActivity activity = getActivity();
        assertThat(activity).isNotNull();
        Spoon.screenshot(activity, "initial_state");
        final LinearLayout directionsList = (LinearLayout) activity.findViewById(R.id.list_directions);
        final EditText mEditTextDirection = (EditText) activity.findViewById(R.id.editText_direction);
        assertThat(directionsList).hasChildCount(0);
        assertThat(mEditTextDirection).hasNoError();
        final View save = activity.findViewById(R.id.recipe_menu_save);
        instrumentation.runOnMainSync(new Runnable() {
            @Override
            public void run() {
                save.performClick();
            }
        });
        instrumentation.waitForIdleSync();
        assertThat(mEditTextDirection).hasError(R.string.at_least_one_direction_required);
    }

    /**
     * Verify that {@link RecipeActivity} is shown with empty tags.
     */
    public void testIsShownWithEmptyTags() throws Exception {
        Recipe recipe = generateRandomRecipe();
        recipe.setTags(null);
        setActivityIntent(makeTestIntent(recipe));
        RecipeActivity activity = getActivity();
        assertThat(activity).isNotNull();
        Spoon.screenshot(activity, "initial_state");
        final LinearLayout tagsList = (LinearLayout) activity.findViewById(R.id.list_tags);
        assertThat(tagsList).hasChildCount(0);
    }

    /**
     * Verify that {@link RecipeActivity} is shown with no ingredients.
     */
    public void testIsShownWithEmptyIngredientsAndSaveFails() throws Exception {
        Recipe recipe = generateRandomRecipe();
        recipe.setIngredients(null);
        setActivityIntent(makeTestIntent(recipe));
        RecipeActivity activity = getActivity();
        assertThat(activity).isNotNull();
        Spoon.screenshot(activity, "initial_state");
        final LinearLayout ingredientsList = (LinearLayout) activity.findViewById(R.id.list_ingredients);
        final EditText mEditTextIngredient = (EditText) activity.findViewById(R.id.editText_ingredient_name);
        assertThat(ingredientsList).hasChildCount(0);
        assertThat(mEditTextIngredient).hasNoError();
        final View save = activity.findViewById(R.id.recipe_menu_save);
        instrumentation.runOnMainSync(new Runnable() {
            @Override
            public void run() {
                save.performClick();
            }
        });
        instrumentation.waitForIdleSync();
        assertThat(mEditTextIngredient).hasError(R.string.at_least_one_ingredient_required);
    }

    /**
     * Verify that {@link RecipeActivity} doesn't save with an empty name.
     */
    public void testFailsSaveWithEmptyName() throws Exception {
        Recipe recipe = generateRandomRecipe();
        setActivityIntent(makeTestIntent(recipe));
        RecipeActivity activity = getActivity();
        assertThat(activity).isNotNull();
        Spoon.screenshot(activity, "initial_state");
        final EditText editTextName = (EditText) activity.findViewById(R.id.editText_recipe_name);
        instrumentation.runOnMainSync(new Runnable() {
            @Override
            public void run() {
                editTextName.setText("");
            }
        });
        instrumentation.waitForIdleSync();
        assertThat(editTextName).hasTextString("").hasNoError();
        final View save = activity.findViewById(R.id.recipe_menu_save);
        instrumentation.runOnMainSync(new Runnable() {
            @Override
            public void run() {
                save.performClick();
            }
        });
        instrumentation.waitForIdleSync();
        assertThat(editTextName).hasError(R.string.blank_field);
    }

    /**
     * Verify that {@link RecipeActivity} shows all the errors.
     */
    public void testShowsAllErrors() throws Exception {
        Recipe recipe = generateRandomRecipe();
        recipe.setName(null);
        recipe.setIngredients(null);
        recipe.setDirections(null);
        setActivityIntent(makeTestIntent(recipe));
        RecipeActivity activity = getActivity();
        assertThat(activity).isNotNull();
        Spoon.screenshot(activity, "initial_state");
        final EditText editTextName = (EditText) activity.findViewById(R.id.editText_recipe_name);
        final EditText mEditTextDirection = (EditText) activity.findViewById(R.id.editText_direction);
        final EditText mEditTextIngredient = (EditText) activity.findViewById(R.id.editText_ingredient_name);
        assertThat(editTextName).hasNoError();
        assertThat(mEditTextDirection).hasNoError();
        assertThat(mEditTextIngredient).hasNoError();
        final View save = activity.findViewById(R.id.recipe_menu_save);
        instrumentation.runOnMainSync(new Runnable() {
            @Override
            public void run() {
                save.performClick();
            }
        });
        instrumentation.waitForIdleSync();
        assertThat(editTextName).hasError(R.string.blank_field);
        assertThat(mEditTextDirection).hasError(R.string.at_least_one_direction_required);
        assertThat(mEditTextIngredient).hasError(R.string.at_least_one_ingredient_required);
    }

}

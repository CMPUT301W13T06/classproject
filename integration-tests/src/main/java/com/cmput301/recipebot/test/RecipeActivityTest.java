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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import com.cmput301.recipebot.R;
import com.cmput301.recipebot.model.Recipe;
import com.cmput301.recipebot.ui.RecipeActivity;
import com.squareup.spoon.Spoon;

import static com.cmput301.recipebot.util.TestDataSetGenerator.getTestRecipe;
import static org.fest.assertions.api.ANDROID.assertThat;

/**
 * Test for existing recipe activity.
 */
public class RecipeActivityTest extends ActivityInstrumentationTestCase2<RecipeActivity> {

    private Instrumentation instrumentation;
    private RecipeActivity activity;
    private Recipe recipe;
    private EditText editTextName;
    private EditText editTextDescription;
    private EditText editTextDirection;
    private EditText editTextIngredient;
    private LinearLayout ingredientList;
    private LinearLayout directionsList;
    private LinearLayout tagsList;
    private ViewPager viewPager;
    private View save;

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
        recipe = getTestRecipe();
        setActivityIntent(makeTestIntent(recipe));
        activity = getActivity();
        editTextName = (EditText) activity.findViewById(R.id.editText_recipe_name);
        editTextDescription = (EditText) activity.findViewById(R.id.editText_recipe_description);
        editTextDirection = (EditText) activity.findViewById(R.id.editText_direction);
        editTextIngredient = (EditText) activity.findViewById(R.id.editText_ingredient_name);
        ingredientList = (LinearLayout) activity.findViewById(R.id.list_ingredients);
        directionsList = (LinearLayout) activity.findViewById(R.id.list_directions);
        tagsList = (LinearLayout) activity.findViewById(R.id.list_tags);
        viewPager = (ViewPager) activity.findViewById(R.id.pager_recipe_images);
        save = activity.findViewById(R.id.recipe_menu_save);
    }

    private Intent makeTestIntent(Recipe recipe) {
        Intent intent = new Intent(instrumentation.getContext(), RecipeActivity.class);
        intent.putExtra(RecipeActivity.EXTRA_RECIPE, recipe);
        return intent;
    }

    /**
     * Verify that {@link RecipeActivity} exists.
     */
    public void testActivityDisplaysFields() throws Exception {
        Spoon.screenshot(activity, "initial_state");
        assertThat(editTextName).hasTextString(recipe.getName());
        assertThat(editTextDescription).hasTextString(recipe.getDescription());
        assertThat(ingredientList).hasChildCount(recipe.getIngredients().size());
        assertThat(directionsList).hasChildCount(recipe.getDirections().size());
        assertThat(tagsList).hasChildCount(recipe.getTags().size());
        assertThat(viewPager.getAdapter()).hasCount(7);
        for (int i = 0; i < recipe.getPhotos().size() + 2; i++) {
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
     * Verify that {@link RecipeActivity} is shown with empty directions and doesn't save.
     */
    public void testIsShownWithEmptyDirectionsAndSaveFails() throws Exception {
        Spoon.screenshot(activity, "initial_state");
        assertThat(editTextDirection).hasNoError();
        clearList(directionsList, "directions");
        instrumentation.runOnMainSync(new Runnable() {
            @Override
            public void run() {
                save.performClick();
            }
        });
        instrumentation.waitForIdleSync();
        assertThat(editTextDirection).hasError(R.string.at_least_one_direction_required);
    }

    /**
     * Deletes all children from a linear layout
     *
     * @param layout
     */
    private void clearList(LinearLayout layout, String tag) {
        int child_count = layout.getChildCount();
        for (int i = 0; i < child_count; i++) {
            final CheckBox child = (CheckBox) layout.getChildAt(0);
            instrumentation.runOnMainSync(new Runnable() {
                @Override
                public void run() {
                    child.setChecked(true);
                }
            });
            Spoon.screenshot(activity, tag + "_checked_" + i);
            instrumentation.waitForIdleSync();
            final View delete = activity.findViewById(R.id.menu_delete_item_from_list);
            instrumentation.runOnMainSync(new Runnable() {
                @Override
                public void run() {
                    delete.performClick();
                }
            });
            instrumentation.waitForIdleSync();
            Spoon.screenshot(activity, tag + "_deleted_" + i);
        }
        assertThat(layout).hasChildCount(0);
    }

    /**
     * Verify that {@link RecipeActivity} is shown with empty tags.
     */
    public void testIsShownWithEmptyTags() throws Exception {
        Spoon.screenshot(activity, "initial_state");
        clearList(tagsList, "tags");
    }

    /**
     * Verify that {@link RecipeActivity} is shown with no ingredients.
     */
    public void testIsShownWithEmptyIngredientsAndSaveFails() throws Exception {
        Spoon.screenshot(activity, "initial_state");
        clearList(ingredientList, "ingredients");
        assertThat(editTextIngredient).hasNoError();
        final View save = activity.findViewById(R.id.recipe_menu_save);
        instrumentation.runOnMainSync(new Runnable() {
            @Override
            public void run() {
                save.performClick();
            }
        });
        instrumentation.waitForIdleSync();
        assertThat(editTextIngredient).hasError(R.string.at_least_one_ingredient_required);
    }

    /**
     * Verify that {@link RecipeActivity} doesn't save with an empty name.
     */
    public void testFailsSaveWithEmptyName() throws Exception {
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
        Spoon.screenshot(activity, "initial_state");
        assertThat(editTextName).hasNoError();
        assertThat(editTextDirection).hasNoError();
        assertThat(editTextIngredient).hasNoError();
        instrumentation.runOnMainSync(new Runnable() {
            @Override
            public void run() {
                editTextName.setText("");
            }
        });
        instrumentation.waitForIdleSync();
        clearList(directionsList, "directions");
        clearList(ingredientList, "ingredients");
        final View save = activity.findViewById(R.id.recipe_menu_save);
        instrumentation.runOnMainSync(new Runnable() {
            @Override
            public void run() {
                save.performClick();
            }
        });
        instrumentation.waitForIdleSync();
        assertThat(editTextName).hasError(R.string.blank_field);
        assertThat(editTextDirection).hasError(R.string.at_least_one_direction_required);
        assertThat(editTextIngredient).hasError(R.string.at_least_one_ingredient_required);
    }

}

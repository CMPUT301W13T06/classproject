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

package com.cmput301.recipebot.test;

import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import com.cmput301.recipebot.ui.AddRecipeActivity;
import com.squareup.spoon.Spoon;

import static org.fest.assertions.api.ANDROID.assertThat;

/**
 * Tests of displaying the authenticator activity
 */
public class AddRecipeActivityTest extends ActivityInstrumentationTestCase2<AddRecipeActivity> {

    private AddRecipeActivity activity;
    private Instrumentation instrumentation;

    /**
     * Create test for {@link com.cmput301.recipebot.ui.MainActivity}
     */
    public AddRecipeActivityTest() {
        super(AddRecipeActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        activity = getActivity();
        instrumentation = getInstrumentation();
    }

    /**
     * Verify that {@link com.cmput301.recipebot.ui.MainActivity} exists
     * TODO: might not be required - {@link org.fest.assertions.api.ANDROID} checks for null with every test.
     */
    public void testActivityExists() {
        assertThat(activity).isNotNull();
        Spoon.screenshot(activity, "initial_state");
    }
}


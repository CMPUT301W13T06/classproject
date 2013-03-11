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
import com.actionbarsherlock.app.ActionBar;
import com.cmput301.recipebot.ui.MainActivity;
import com.squareup.spoon.Spoon;

import static org.fest.assertions.api.ANDROID.assertThat;

/**
 * Tests of displaying the authenticator activity
 */
public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private MainActivity activity;
    private Instrumentation instrumentation;

    /**
     * Create test for {@link com.cmput301.recipebot.ui.MainActivity}
     */
    public MainActivityTest() {
        super(MainActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        activity = getActivity();
        instrumentation = getInstrumentation();
    }

    /**
     * Verify that {@link MainActivity} exists
     * TODO: might not be required - {@link org.fest.assertions.api.ANDROID} checks for null with every test.
     */
    public void testActivityExists() {
        assertThat(activity).isNotNull();
        Spoon.screenshot(activity, "initial_state");
    }

    /**
     * Verify that two tabs are shown to the user.
     * TODO : verify the titles themselves?
     */
    public void testTabsExist() {
        Spoon.screenshot(activity, "initial_state");
        ActionBar actionBar = activity.getSupportActionBar();
        assertEquals(2, actionBar.getTabCount());
    }
}


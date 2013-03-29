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
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.test.ActivityInstrumentationTestCase2;
import com.cmput301.recipebot.R;
import com.cmput301.recipebot.ui.MainActivity;
import com.cmput301.recipebot.util.AppConstants;
import com.squareup.spoon.Spoon;

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
        instrumentation = getInstrumentation();
        setupTestPreferences(instrumentation);
        activity = getActivity();
    }

    /**
     * Set up some Preferences for testing. {@link MainActivity} checks these
     * on it's start, and launches {@link com.cmput301.recipebot.ui.GetUserActivity} if doesn't find these.
     */
    private void setupTestPreferences(Instrumentation instrumentation) {
        Context context = instrumentation.getTargetContext();
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(AppConstants.KEY_USER_EMAIL, "f2prateek@gmail.com");
        editor.putString(AppConstants.KEY_USER_NAME, "Prateek Srivastava");
        editor.commit();
    }

    /**
     * Verify that two tabs are shown to the user.
     */
    public void testBothTabsShown() {
        final ViewPager viewPager = (ViewPager) activity.findViewById(R.id.pager);
        instrumentation.runOnMainSync(new Runnable() {
            @Override
            public void run() {
                viewPager.setCurrentItem(0);
            }
        });
        instrumentation.waitForIdleSync();
        Spoon.screenshot(activity, "pantry");
        instrumentation.runOnMainSync(new Runnable() {
            @Override
            public void run() {
                viewPager.setCurrentItem(1);
            }
        });
        instrumentation.waitForIdleSync();
        Spoon.screenshot(activity, "saved_recipes");
    }
}


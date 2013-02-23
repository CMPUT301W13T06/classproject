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

import android.test.ActivityInstrumentationTestCase2;
import com.cmput301.recipebot.ui.MainActivity;
import com.squareup.spoon.Spoon;

/**
 * Tests of displaying the authenticator activity
 */
public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

    /**
     * Create test for {@link com.cmput301.recipebot.ui.MainActivity}
     */
    public MainActivityTest() {
        super(MainActivity.class);
    }

    /**
     * Verify activity exists
     */
    public void testActivityExists() {
        assertNotNull(getActivity());
        Spoon.screenshot(getActivity(), "initial_state");
    }
}


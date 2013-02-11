

package com.cmput301.recipebot.test;

import android.test.ActivityInstrumentationTestCase2;
import com.cmput301.recipebot.ui.MainActivity;


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
    }
}


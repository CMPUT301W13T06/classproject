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

package com.cmput301.recipebot.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import com.actionbarsherlock.app.ActionBar;
import com.cmput301.recipebot.R;
import com.cmput301.recipebot.ui.fragments.PantryFragment;
import com.cmput301.recipebot.ui.fragments.SavedRecipesFragment;

/**
 * Main Activity, that shows two fragments {@link PantryFragment} and {@link SavedRecipesFragment}.
 */
public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        setupTabs();
    }

    /**
     * Setup the tabs two display our fragments.
     */
    private void setupTabs() {
        ActionBar actionBar = getSupportActionBar();
        ActionBar.Tab tab = actionBar.newTab()
                .setText(R.string.fragment_pantry_title)
                .setTabListener(new TabListener<PantryFragment>(
                        this, "pantry", PantryFragment.class));
        actionBar.addTab(tab);

        tab = actionBar.newTab()
                .setText(R.string.fragment_saved_recipes_title)
                .setTabListener(new TabListener<SavedRecipesFragment>(
                        this, "saved recipes", SavedRecipesFragment.class));
        actionBar.addTab(tab);
    }

    /**
     * Simply switches between our tabs.
     *
     * @param <T>
     */
    public static class TabListener<T extends Fragment> implements ActionBar.TabListener {
        private final FragmentActivity mActivity;
        private final String mTag;
        private final Class<T> mClass;
        private Fragment mFragment;

        /**
         * Constructor used each time a new tab is created.
         *
         * @param activity The host Activity, used to instantiate the fragment
         * @param tag      The identifier tag for the fragment
         * @param clz      The fragment's Class, used to instantiate the fragment
         */
        public TabListener(FragmentActivity activity, String tag, Class<T> clz) {
            mActivity = activity;
            mTag = tag;
            mClass = clz;
        }

    /* The following are each of the ActionBar.TabListener callbacks */

        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
            // Check if the fragment is already initialized
            if (mFragment == null) {
                // If not, instantiate and add it to the activity
                mFragment = Fragment.instantiate(mActivity, mClass.getName());
                ft.add(android.R.id.content, mFragment, mTag);
            } else {
                // If it exists, simply attach it in order to show it
                ft.attach(mFragment);
            }
        }

        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
            if (mFragment != null) {
                // Detach the fragment, because another one is being attached
                ft.detach(mFragment);
            }
        }

        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
            // User selected the already selected tab. Usually do nothing.
        }
    }

}

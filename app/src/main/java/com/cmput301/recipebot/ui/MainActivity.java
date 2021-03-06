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

package com.cmput301.recipebot.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.actionbarsherlock.widget.SearchView;
import com.cmput301.recipebot.R;
import com.cmput301.recipebot.model.RecipeModel;
import com.cmput301.recipebot.model.beans.Recipe;
import com.cmput301.recipebot.model.network.ESClient;
import com.cmput301.recipebot.ui.fragments.PantryFragment;
import com.cmput301.recipebot.ui.fragments.SavedRecipesGridFragment;
import com.cmput301.recipebot.util.AppConstants;
import com.cmput301.recipebot.util.NetworkUtils;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import roboguice.inject.InjectView;

import java.util.ArrayList;

import static com.cmput301.recipebot.util.LogUtils.makeLogTag;

/**
 * Main Activity, that shows two fragments {@link PantryFragment} and {@link SavedRecipesGridFragment}.
 */
public class MainActivity extends BaseActivity implements SearchView.OnQueryTextListener {

    private static final String LOGTAG = makeLogTag(MainActivity.class);

    @InjectView(R.id.pager)
    ViewPager mViewPager;

    private TabsAdapter mTabsAdapter;
    private SearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPreferences();
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_main);
        // Explicitly required for pre-4.0 devices
        setSupportProgressBarIndeterminateVisibility(false);
        setupTabs(savedInstanceState);
    }

    private void checkPreferences() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String email = sharedPref.getString(AppConstants.KEY_USER_EMAIL, null);
        String name = sharedPref.getString(AppConstants.KEY_USER_NAME, null);
        if (email == null || name == null) {
            Intent intent = new Intent(MainActivity.this, GetUserActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        } else {
            Crouton.makeText(this, getString(R.string.hello_user, name), Style.CONFIRM).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add_recipe:
                addRecipe();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.activity_main, menu);
        MenuItem searchItem = menu.findItem(R.id.menu_search_saved_recipes);
        mSearchView = (SearchView) searchItem.getActionView();
        setupSearchView(searchItem);
        return true;
    }

    private void setupSearchView(MenuItem searchItem) {
        searchItem.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM
                | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
        mSearchView.setOnQueryTextListener(this);
    }

    /**
     * Start an activity to add a new Recipe.
     */
    private void addRecipe() {
        Intent intent = new Intent(this, EditRecipeActivity.class);
        startActivity(intent);
    }

    /**
     * Setup the tabs two display our fragments.
     */
    private void setupTabs(Bundle savedInstanceState) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        mTabsAdapter = new TabsAdapter(this, mViewPager);
        mTabsAdapter.addTab(actionBar.newTab().setText(R.string.fragment_pantry_title),
                PantryFragment.class, null);
        mTabsAdapter.addTab(actionBar.newTab().setText(R.string.fragment_saved_recipes_title),
                SavedRecipesGridFragment.class, null);

        if (savedInstanceState != null) {
            actionBar.setSelectedNavigationItem(savedInstanceState.getInt("tab", 0));
        }

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        new SearchTask(this, query).execute();
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    private class SearchTask extends AsyncTask<Void, Void, ArrayList<Recipe>> {
        String query;
        Context context;

        public SearchTask(Context context, String query) {
            this.context = context;
            this.query = query;
        }

        @Override
        protected void onPreExecute() {
            setSupportProgressBarIndeterminateVisibility(true);
            super.onPreExecute();
        }

        @Override
        protected ArrayList<Recipe> doInBackground(Void... params) {
            if (NetworkUtils.isConnected(context)) {
                return networkSearch();
            } else {
                return localSearch();
            }
        }

        private ArrayList<Recipe> localSearch() {
            if (query.charAt(0) == '@') {
                return RecipeModel.getInstance(context).searchRecipesFromUser(query.substring(1));
            } else if (query.charAt(0) == '#') {
                return RecipeModel.getInstance(context).searchRecipesFromTag(query.substring(1));
            } else {
                return RecipeModel.getInstance(context).searchRecipes(query);
            }
        }

        private ArrayList<Recipe> networkSearch() {
            ESClient client = new ESClient();
            if (query.charAt(0) == '@') {
                return client.searchRecipesFromUser(query.substring(1));
            } else if (query.charAt(0) == '#') {
                return client.searchRecipesFromTag(query.substring(1));
            } else {
                return client.searchRecipes(query);
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Recipe> recipes) {
            setSupportProgressBarIndeterminateVisibility(false);
            Intent intent = new Intent(MainActivity.this, SearchRecipeActivity.class);
            intent.putParcelableArrayListExtra(SearchRecipeActivity.EXTRA_RECIPE_LIST, recipes);
            intent.putExtra(SearchRecipeActivity.EXTRA_SEARCH_TERM, query);
            startActivity(intent);
            super.onPostExecute(recipes);
        }
    }

    /**
     * This is a helper class that implements the management of tabs and all
     * details of connecting a ViewPager with associated TabHost.  It relies on a
     * trick.  Normally a tab host has a simple API for supplying a View or
     * Intent that each tab will show.  This is not sufficient for switching
     * between pages.  So instead we make the content part of the tab host
     * 0dp high (it is not shown) and the TabsAdapter supplies its own dummy
     * view to show as the tab content.  It listens to changes in tabs, and takes
     * care of switch to the correct paged in the ViewPager whenever the selected
     * tab changes.
     */
    public static class TabsAdapter extends FragmentPagerAdapter
            implements ActionBar.TabListener, ViewPager.OnPageChangeListener {
        private final Context mContext;
        private final ActionBar mActionBar;
        private final ViewPager mViewPager;
        private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();

        static final class TabInfo {
            private final Class<?> clss;
            private final Bundle args;

            TabInfo(Class<?> _class, Bundle _args) {
                clss = _class;
                args = _args;
            }
        }

        public TabsAdapter(SherlockFragmentActivity activity, ViewPager pager) {
            super(activity.getSupportFragmentManager());
            mContext = activity;
            mActionBar = activity.getSupportActionBar();
            mViewPager = pager;
            mViewPager.setAdapter(this);
            mViewPager.setOnPageChangeListener(this);
        }

        public void addTab(ActionBar.Tab tab, Class<?> clss, Bundle args) {
            TabInfo info = new TabInfo(clss, args);
            tab.setTag(info);
            tab.setTabListener(this);
            mTabs.add(info);
            mActionBar.addTab(tab);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mTabs.size();
        }

        @Override
        public Fragment getItem(int position) {
            TabInfo info = mTabs.get(position);
            return Fragment.instantiate(mContext, info.clss.getName(), info.args);
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            mActionBar.setSelectedNavigationItem(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }

        @Override
        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
            Object tag = tab.getTag();
            for (int i = 0; i < mTabs.size(); i++) {
                if (mTabs.get(i) == tag) {
                    mViewPager.setCurrentItem(i);
                }
            }
        }

        @Override
        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
        }

        @Override
        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("tab", getSupportActionBar().getSelectedNavigationIndex());
    }

}

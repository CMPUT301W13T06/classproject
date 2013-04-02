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

package com.cmput301.recipebot.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.cmput301.recipebot.R;
import com.cmput301.recipebot.model.PantryModel;
import com.cmput301.recipebot.model.RecipeModel;
import com.cmput301.recipebot.model.beans.Ingredient;
import com.cmput301.recipebot.model.beans.Recipe;
import com.cmput301.recipebot.model.network.ESClient;
import com.cmput301.recipebot.ui.SearchRecipeActivity;
import com.cmput301.recipebot.util.NetworkUtils;
import com.github.rtyley.android.sherlock.roboguice.fragment.RoboSherlockListFragment;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

import java.util.ArrayList;
import java.util.List;

import static com.cmput301.recipebot.util.LogUtils.makeLogTag;

/**
 * A fragment that shows a list of items in the pantry.
 */
public class PantryFragment extends RoboSherlockListFragment implements View.OnClickListener, PantryModel.PantryView {

    private static final String LOGTAG = makeLogTag(PantryFragment.class);

    private EditText mEditTextName;
    private EditText mEditTextQuantity;
    private EditText mEditTextUnit;

    private List<Ingredient> mPantryItems;
    private List<CompoundButton> selection;
    private PantryListAdapter mAdapter;
    private ActionMode mActionMode;

    @Override
    public void onResume() {
        PantryModel.getInstance(getSherlockActivity()).addView(this);
        super.onResume();
    }

    @Override
    public void onPause() {
        PantryModel.getInstance(getSherlockActivity()).deleteView(this);
        super.onPause();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        setListShown(false);
        fillView();
    }

    /**
     * fill the view with and grab references.
     */
    private void fillView() {
        mPantryItems = PantryModel.getInstance(getSherlockActivity()).loadPantry();
        LayoutInflater layoutInflater = getSherlockActivity().getLayoutInflater();
        View header = layoutInflater.inflate(R.layout.add_ingredient_widget, null);
        header.findViewById(R.id.button_add_ingredient).setOnClickListener(this);
        mEditTextName = (EditText) header.findViewById(R.id.editText_ingredient_name);
        mEditTextQuantity = (EditText) header.findViewById(R.id.editText_ingredient_quantity);
        mEditTextUnit = (EditText) header.findViewById(R.id.editText_ingredient_unit);
        getListView().addHeaderView(header);
        mAdapter = new PantryListAdapter(mPantryItems);
        setListAdapter(mAdapter);
        setListShown(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_pantry, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_select_all:
                selectAll();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_add_ingredient:
                addIngredient();
        }
    }

    /**
     * Add an pantry item to the database.
     */
    private void addIngredient() {
        if (isEditTextEmpty(mEditTextName)) {
            // Name should not be empty.
            mEditTextName.setError(getResources().getString(R.string.blank_field));
            return;
        } else {
            mEditTextName.setError(null);
        }

        String name = mEditTextName.getText().toString();
        // Don't parse the float if the field is empty.
        float quantity = isEditTextEmpty(mEditTextQuantity) ? 0.0f : Float.parseFloat(mEditTextQuantity.getText().toString());
        String unit = mEditTextUnit.getText().toString();
        Ingredient item = new Ingredient(name, quantity, unit);
        PantryModel.getInstance(getSherlockActivity()).insertPantryItem(item);

        //Sanitize the input
        mEditTextName.setText(null);
        mEditTextQuantity.setText(null);
        mEditTextUnit.setText(null);
    }

    /**
     * Checks if an {@link EditText} field is empty.
     *
     * @param editText {@link EditText} to check.
     * @return true if editText is empty.
     */
    private boolean isEditTextEmpty(EditText editText) {
        return editText.getText().toString().isEmpty();
    }

    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.fragment_pantry_cab, menu);
            return true;
        }

        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu_search_from_pantry:
                    searchSelected();
                    mode.finish();
                    return true;
                case R.id.menu_delete_from_pantry:
                    deleteSelected();
                    mode.finish();
                    return true;
                default:
                    return false;
            }
        }

        // Called when the user exits the action mode
        public void onDestroyActionMode(ActionMode mode) {
            for (CompoundButton button : selection) {
                button.setOnCheckedChangeListener(null);
                button.setChecked(false);
                button.setOnCheckedChangeListener(onCheckedChangeListener);
            }
            mActionMode = null;
        }
    };

    private void searchSelected() {
        ArrayList<Ingredient> items = new ArrayList<Ingredient>();
        for (CompoundButton button : selection) {
            items.add((Ingredient) button.getTag());
        }
        new SearchRecipesByIngredientsTask().execute(items);
    }

    private class SearchRecipesByIngredientsTask extends AsyncTask<ArrayList<Ingredient>, Void, ArrayList<Recipe>> {
        @Override
        protected void onPreExecute() {
            getSherlockActivity().setSupportProgressBarIndeterminateVisibility(true);
            super.onPreExecute();
        }

        @Override
        protected ArrayList<Recipe> doInBackground(ArrayList<Ingredient>... arrayLists) {
            ArrayList<Ingredient> ingredients = arrayLists[0];
            if (NetworkUtils.isConnected(getSherlockActivity())) {
                ESClient client = new ESClient();
                return client.searchRecipes(ingredients, true);
            } else {
                return RecipeModel.getInstance(getSherlockActivity()).searchRecipesByIngredients(ingredients);
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Recipe> recipes) {
            getSherlockActivity().setSupportProgressBarIndeterminateVisibility(false);
            Intent intent = new Intent(getSherlockActivity(), SearchRecipeActivity.class);
            intent.putParcelableArrayListExtra(SearchRecipeActivity.EXTRA_RECIPE_LIST, recipes);
            intent.putExtra(SearchRecipeActivity.EXTRA_SEARCH_TERM, getString(R.string.search_ingredients));
            startActivity(intent);
            super.onPostExecute(recipes);
        }
    }

    private void deleteSelected() {
        if (selection == null) {
            Log.e(LOGTAG, "Shouldn't be here!");
        }
        for (CompoundButton button : selection) {
            Ingredient ingredient = (Ingredient) button.getTag();
            PantryModel.getInstance(getSherlockActivity()).deletePantryItem(ingredient.getName());
        }
        Crouton.makeText(getSherlockActivity(), R.string.pantry_item_deleted, Style.ALERT).show();
    }

    /**
     * Select all ingredients.
     */
    public void selectAll() {
        final ListView listView = getListView();
        for (int i = 1; i < listView.getChildCount(); i++) {
            CheckBox cb = (CheckBox) listView.getChildAt(i);
            cb.setChecked(true);
        }
    }

    private CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
            if (isChecked) {
                if (mActionMode == null) {
                    selection = new ArrayList<CompoundButton>();
                    mActionMode = getSherlockActivity().startActionMode(mActionModeCallback);
                }
                selection.add(compoundButton);
                mActionMode.setTitle(getSherlockActivity().getResources().getString(R.string.count_items_selected, selection.size()));
            } else {
                selection.remove(compoundButton);
                mActionMode.setTitle(getSherlockActivity().getResources().getString(R.string.count_items_selected, selection.size()));
                //If no more items, finish the action mode explicitly
                if (selection.size() == 0 && mActionMode != null) {
                    mActionMode.finish();
                }
            }
        }
    };

    @Override
    public void update(ArrayList<Ingredient> ingredientList) {
        mPantryItems = ingredientList;
        mAdapter.swapData(mPantryItems);
    }

    public class PantryListAdapter extends BaseAdapter {

        List<Ingredient> ingredients;

        public PantryListAdapter(List<Ingredient> ingredients) {
            this.ingredients = ingredients;
        }

        @Override
        public int getCount() {
            return ingredients.size();
        }

        @Override
        public Object getItem(int position) {
            return ingredients.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final View view;
            if (convertView == null) {
                LayoutInflater layoutInflater = (LayoutInflater) getSherlockActivity().
                        getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = layoutInflater.inflate(R.layout.checkbox_view, parent, false);
            } else {
                view = (convertView);
            }

            Ingredient ingredient = (Ingredient) getItem(position);
            CheckBox box = (CheckBox) view.findViewById(R.id.check_box);
            box.setTag(ingredient);
            box.setText(ingredient.toString());
            box.setOnCheckedChangeListener(onCheckedChangeListener);
            return view;
        }

        public void swapData(List<Ingredient> ingredients) {
            this.ingredients = ingredients;
            notifyDataSetChanged();
        }
    }

}
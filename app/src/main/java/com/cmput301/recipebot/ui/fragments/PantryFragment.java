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
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.cmput301.recipebot.R;
import com.cmput301.recipebot.model.Ingredient;
import com.github.rtyley.android.sherlock.roboguice.fragment.RoboSherlockListFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment that shows a list of items in the pantry.
 */
public class PantryFragment extends RoboSherlockListFragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private EditText mEdiText;
    ArrayList<Ingredient> mPantryItems;
    List<CompoundButton> selection;

    PantryListAdapter mAdapter;

    protected ActionMode mActionMode;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setHasOptionsMenu(true);
        fillView();

    }

    private void fillView() {
        setListShown(false);

        setEmptyText(getSherlockActivity().getResources().getString(R.string.no_pantry_items));

        LayoutInflater layoutInflater = getSherlockActivity().getLayoutInflater();

        if (getListView().getHeaderViewsCount() == 0) {
            View header = layoutInflater.inflate(R.layout.fragment_pantry_header, null);
            header.findViewById(R.id.button_add_pantry).setOnClickListener(this);
            mEdiText = (EditText) header.findViewById(R.id.editText_pantry);
            getListView().addHeaderView(header);
        }

        mPantryItems = new ArrayList<Ingredient>();
        mPantryItems.add(new Ingredient("Eggs", "nos.", 2f));
        mPantryItems.add(new Ingredient("Milk", "ml", 500f));
        mPantryItems.add(new Ingredient("Bread", "ml", 500f));
        mPantryItems.add(new Ingredient("Buter", "ml", 500f));

        mAdapter = new PantryListAdapter(mPantryItems, this);
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
            case R.id.menu_add_pantry:
                addEntry();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void addEntry() {
        Ingredient item = new Ingredient();
        item.setName(mEdiText.getText().toString());
        mPantryItems.add(item);
        mAdapter.swapData(mPantryItems);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_add_pantry:
                addEntry();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

        if (isChecked) {
            if (mActionMode == null) {
                selection = new ArrayList<CompoundButton>();
                mActionMode = getSherlockActivity().startActionMode(mActionModeCallback);
            }
            selection.add(compoundButton);
        } else {
            // Unselected, remove fom items
            selection.remove(compoundButton);
            //If no more items, finish the action mode explicitly
            if (selection.size() == 0 && mActionMode != null) {
                mActionMode.finish();
            }
        }
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
                    Toast.makeText(getSherlockActivity(), "TODO: search for " + selection.size(), Toast.LENGTH_SHORT).show();
                    mode.finish();
                    return true;
                default:
                    return false;
            }
        }

        // Called when the user exits the action mode
        public void onDestroyActionMode(ActionMode mode) {
            mActionMode = null;
            fillView();
        }
    };

    public class PantryListAdapter extends BaseAdapter {

        ArrayList<Ingredient> ingredients;
        CompoundButton.OnCheckedChangeListener listener;

        public PantryListAdapter(ArrayList<Ingredient> ingredients, CompoundButton.OnCheckedChangeListener listener) {
            this.ingredients = ingredients;
            this.listener = listener;
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
                view = layoutInflater.inflate(R.layout.item_pantry, parent, false);
            } else {
                view = (convertView);
            }

            Ingredient ingredient = (Ingredient) getItem(position);
            CheckBox box = (CheckBox) view.findViewById(R.id.check_box);
            box.setTag(ingredient);
            box.setText(ingredient.getName());
            box.setOnCheckedChangeListener(listener);

            return view;
        }

        public void swapData(ArrayList<Ingredient> ingredients) {
            this.ingredients = ingredients;
            notifyDataSetChanged();
        }
    }

}
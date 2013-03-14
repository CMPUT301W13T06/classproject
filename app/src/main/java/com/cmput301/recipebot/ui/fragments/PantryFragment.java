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

package com.cmput301.recipebot.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.cmput301.recipebot.R;
import com.cmput301.recipebot.model.PantryItem;
import com.cmput301.recipebot.ui.adapters.PantryListAdapter;
import com.github.rtyley.android.sherlock.roboguice.fragment.RoboSherlockListFragment;

import java.util.ArrayList;

/**
 * A fragment that shows a list of items in the pantry.
 */
public class PantryFragment extends RoboSherlockListFragment implements View.OnClickListener {

    private EditText mEdiText;
    ArrayList<PantryItem> mItems;
    PantryListAdapter mAdapter;

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

        View header = layoutInflater.inflate(R.layout.fragment_pantry_header, null);
        header.findViewById(R.id.button_add_pantry).setOnClickListener(this);
        mEdiText = (EditText) header.findViewById(R.id.editText_pantry);
        getListView().addHeaderView(header);

        mItems = new ArrayList<PantryItem>();
        mItems.add(new PantryItem("Eggs"));
        mItems.add(new PantryItem("Milk"));
        mItems.add(new PantryItem("Butter"));
        mItems.add(new PantryItem("Toast"));
        mItems.add(new PantryItem("Crouton"));

        mAdapter = new PantryListAdapter(getSherlockActivity(), mItems);
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
        PantryItem item = new PantryItem(mEdiText.getText().toString());
        mItems.add(item);
        mAdapter.swapData(mItems);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_add_pantry:
                addEntry();
        }
    }
}
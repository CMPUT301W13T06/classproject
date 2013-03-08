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
import android.widget.ArrayAdapter;
import android.widget.Toast;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.cmput301.recipebot.R;
import com.github.rtyley.android.sherlock.roboguice.fragment.RoboSherlockListFragment;

/**
 * A fragment that shows a list of items in the pantry.
 */
public class PantryFragment extends RoboSherlockListFragment {

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setHasOptionsMenu(true);
        fillView();

    }

    private void fillView() {
        setListShown(false);

        // TODO : fill with stuff
        setEmptyText(getSherlockActivity().getResources().getString(R.string.no_pantry_items));

        String[] values = new String[]{"Android", "iPhone", "WindowsMobile",
                "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
                "Linux", "OS/2"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getSherlockActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, values);

        setListAdapter(adapter);

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
        // TODO : add an entry here
        Toast.makeText(getSherlockActivity(), "TODO: Add to Pantry", Toast.LENGTH_LONG).show();
    }

}
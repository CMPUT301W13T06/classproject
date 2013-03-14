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

package com.cmput301.recipebot.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import com.cmput301.recipebot.R;
import com.cmput301.recipebot.model.PantryItem;

import java.util.ArrayList;

public class PantryListAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<PantryItem> mItems;

    public PantryListAdapter(Context context, ArrayList<PantryItem> items) {
        mContext = context;
        mItems = items;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View view;
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.item_pantry, parent, false);
        } else {
            view = (convertView);
        }

        CheckBox box = (CheckBox) view.findViewById(R.id.checkBox);
        box.setText(((PantryItem) getItem(position)).getName());

        return view;
    }

    public void swapData(ArrayList<PantryItem> items) {
        mItems = items;
        notifyDataSetChanged();
    }
}

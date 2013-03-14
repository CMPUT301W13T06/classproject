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
import android.widget.ImageView;
import com.cmput301.recipebot.R;
import com.cmput301.recipebot.model.Recipe;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class RecipeGridAdapter extends BaseAdapter {

    private List<Recipe> mRecipes;
    private Context mContext;

    /**
     * Construct the Adapter
     *
     * @param context everything needs a context =(
     * @param recipes List of {@link Recipe} to display
     */
    public RecipeGridAdapter(Context context, List<Recipe> recipes) {
        mRecipes = recipes;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mRecipes.size();
    }

    @Override
    public Object getItem(int position) {
        return mRecipes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ImageView imageView;
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            imageView = (ImageView) layoutInflater.inflate(R.layout.item_recipe_image, parent, false);
        } else {
            imageView = (ImageView) convertView;
        }

        ImageLoader.getInstance().displayImage(mRecipes.get(position).getPhotos().get(0).toString(), imageView);

        return imageView;
    }
}

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
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.*;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.cmput301.recipebot.R;
import com.cmput301.recipebot.model.Recipe;
import com.google.gson.Gson;
import roboguice.inject.InjectView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import static com.cmput301.recipebot.util.LogUtils.makeLogTag;

public abstract class AbstractRecipeActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {

    public static final String EXTRA_RECIPE = "EXTRA_RECIPE";

    private static final String LOGTAG = makeLogTag(AbstractRecipeActivity.class);
    protected static final int TYPE_INGREDIENT = 100;
    protected static final int TYPE_DIRECTION = 200;
    protected static final int TYPE_TAG = 300;

    @InjectView(R.id.pager_recipe_images)
    ViewPager mViewPhotos;
    @InjectView(R.id.list_directions)
    LinearLayout mListViewDirections;
    @InjectView(R.id.list_ingredients)
    LinearLayout mListViewIngredients;
    @InjectView(R.id.list_tags)
    LinearLayout mListViewTags;

    @InjectView(R.id.editText_recipe_name)
    EditText mEditTextRecipeName;
    @InjectView(R.id.editText_recipe_description)
    EditText mEditTextRecipeDescription;
    @InjectView(R.id.editText_direction)
    EditText mEditTextDirection;
    @InjectView(R.id.button_add_direction)
    ImageButton mButtonAddDirection;
    @InjectView(R.id.editText_ingredient_name)
    EditText mEditTextIngredientName;
    @InjectView(R.id.editText_ingredient_quantity)
    EditText mEditTextIngredientQuantity;
    @InjectView(R.id.editText_ingredient_unit)
    EditText mEditTextIngredientUnit;
    @InjectView(R.id.button_add_ingredient)
    ImageButton mButtonAddIngredient;
    @InjectView(R.id.editText_tag)
    EditText mEditTextTag;
    @InjectView(R.id.button_add_tag)
    ImageButton mButtonAddTag;

    protected Recipe mRecipe;

    // Keep a global reference to this since we want to update the recipe when user clicks save.
    private com.actionbarsherlock.widget.ShareActionProvider mShareActionProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        if (getIntent().getExtras() != null) {
            mRecipe = getIntent().getParcelableExtra(EXTRA_RECIPE);
        }
        fillView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.activity_recipe, menu);
        MenuItem actionItem = menu.findItem(R.id.recipe_menu_share);
        mShareActionProvider = (com.actionbarsherlock.widget.ShareActionProvider) actionItem.getActionProvider();
        mShareActionProvider.setShareHistoryFileName(com.actionbarsherlock.widget.ShareActionProvider.DEFAULT_SHARE_HISTORY_FILE_NAME);
        if (mRecipe != null) {
            new WriteRecipeToFileTask().execute(mRecipe);
        }
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Fill our views.
     */
    protected void fillView() {
        setPhotos();
        mEditTextRecipeName.setText(mRecipe.getName());
        mEditTextRecipeDescription.setText(mRecipe.getDescription());
        fillListLayout(mListViewDirections, mRecipe.getDirections(), TYPE_DIRECTION);
        fillListLayout(mListViewIngredients, mRecipe.getIngredients(), TYPE_INGREDIENT);
        fillListLayout(mListViewTags, mRecipe.getTags(), TYPE_TAG);
    }

    public abstract void setPhotos();

    /**
     * A method that fills a {@link LinearLayout} with data from a List of data.
     * We don't use {@link android.widget.ListView} since we don't need scrolling, our {@link android.widget.ScrollView} handles
     * that for us.
     *
     * @param listDirections the layout to fill
     * @param dataset        the data that should be displayed.
     */
    protected void fillListLayout(LinearLayout listDirections, List dataset, int type) {
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //Sanitize the view
        listDirections.removeAllViews();

        for (int i = 0; i < dataset.size(); i++) {
            Object data = dataset.get(i);
            CheckBox checkBox = (CheckBox) layoutInflater.inflate(R.layout.checkbox_view, null);
            checkBox.setText(data.toString());
            TaggedItem item = new TaggedItem();
            item.type = type;
            item.data = data;
            checkBox.setTag(item);
            checkBox.setOnCheckedChangeListener(this);
            listDirections.addView(checkBox, i);
        }
    }

    class TaggedItem<T> {
        T data;
        int type;
    }

    class WriteRecipeToFileTask extends AsyncTask<Recipe, Void, File> {

        private static final String STORAGE_DIRECTORY = "RecipeBot";

        @Override
        protected File doInBackground(Recipe... params) {
            Recipe recipe = params[0];
            File file = new File(getStorageDirectory(), recipe.getId() + ".json");
            FileOutputStream outputStream;
            Gson gson = new Gson();

            try {
                outputStream = new FileOutputStream(file);
                outputStream.write(gson.toJson(recipe, Recipe.class).getBytes());
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return file;
        }

        @Override
        protected void onPostExecute(File file) {
            super.onPostExecute(file);
            mShareActionProvider.setShareIntent(createShareIntent(mRecipe, file));
        }

        protected File getStorageDirectory() {
            // Get the directory for the user's public pictures directory.
            File file = new File(Environment.getExternalStorageDirectory(), STORAGE_DIRECTORY);
            if (!file.exists()) {
                if (!file.mkdirs()) {
                    Log.e(LOGTAG, "Directory not created, " + file.getAbsolutePath());
                }
            }
            return file;
        }

        /* Checks if external storage is available for read and write */
        protected boolean isExternalStorageWritable() {
            String state = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                return true;
            }
            return false;
        }

        /* Checks if external storage is available to at least read */
        protected boolean isExternalStorageReadable() {
            String state = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state) ||
                    Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
                return true;
            }
            return false;
        }
    }

    /**
     * Creates a sharing {@link android.content.Intent}.
     * Shares mRecipe, doesn't get it from the UI.
     *
     * @return The sharing intent.
     */
    private static Intent createShareIntent(Recipe recipe, File file) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, recipe.getName());
        shareIntent.putExtra(Intent.EXTRA_TITLE, recipe.getName());
        shareIntent.putExtra(Intent.EXTRA_TEXT, recipe.toString());
        shareIntent.setType("application/json");
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        return shareIntent;
    }

}

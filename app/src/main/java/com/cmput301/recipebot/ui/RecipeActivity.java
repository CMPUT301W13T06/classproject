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
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.ShareActionProvider;
import com.cmput301.recipebot.R;
import com.cmput301.recipebot.model.Ingredient;
import com.cmput301.recipebot.model.Recipe;
import com.cmput301.recipebot.model.RecipeBotController;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import roboguice.inject.InjectView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.cmput301.recipebot.util.LogUtils.makeLogTag;

/**
 * An Activity that allows user to add a recipe.
 */
public class RecipeActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    private static final String LOGTAG = makeLogTag(RecipeActivity.class);
    public static final String EXTRA_RECIPE = "EXTRA_RECIPE";

    private static final int TYPE_INGREDIENT = 1;
    private static final int TYPE_DIRECTION = 2;
    private static final int TYPE_TAG = 3;

    private static final int RESULT_LOAD_IMAGE = 458;
    private static final int TAKE_PICTURE = 531;
    private Uri cameraImageUri;

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

    private ActionMode mActionMode;
    private CompoundButton selectedCheckBox;

    private String mRecipeID;
    private String mRecipeName;
    private String mRecipeDescription;
    private ArrayList<Ingredient> mRecipeIngredients;
    private ArrayList<String> mRecipeDirections;
    private ArrayList<String> mRecipePhotos;
    private ArrayList<String> mRecipeTags;

    // This keeps track of our Recipe, is updated when user clicks save.
    private Recipe mRecipe;

    // Keep a global reference to this since we want to update the recipe when user clicks save.
    private ShareActionProvider mShareActionProvider;

    private RecipeBotController mController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mController = new RecipeBotController(this);
        setContentView(R.layout.activity_recipe);
        if (getIntent().getExtras() != null) {
            mRecipe = getIntent().getParcelableExtra(EXTRA_RECIPE);
            mRecipeID = mRecipe.getId();
            mRecipeName = mRecipe.getName();
            mRecipeDescription = mRecipe.getDescription();
            mRecipeIngredients = mRecipe.getIngredients();
            mRecipeDirections = mRecipe.getDirections();
            mRecipePhotos = mRecipe.getPhotos();
            mRecipeTags = mRecipe.getTags();
            fillView(mRecipe);
        } else {
            mRecipe = null;
            mRecipeID = null;
            mRecipeName = null;
            mRecipeDescription = null;
            mRecipeDirections = new ArrayList<String>();
            mRecipeIngredients = new ArrayList<Ingredient>();
            mRecipePhotos = new ArrayList<String>();
            mRecipeTags = new ArrayList<String>();
            mViewPhotos.setAdapter(new ImagePagerAdapter(mRecipePhotos));
        }
        mButtonAddDirection.setOnClickListener(this);
        mButtonAddIngredient.setOnClickListener(this);
        mButtonAddTag.setOnClickListener(this);
    }

    /**
     * Fill our Views.
     */
    private void fillView(Recipe recipe) {
        mViewPhotos.setAdapter(new ImagePagerAdapter(recipe.getPhotos()));
        mEditTextRecipeName.setText(recipe.getName());
        mEditTextRecipeDescription.setText(recipe.getDescription());
        fillListLayout(mListViewDirections, recipe.getDirections(), TYPE_DIRECTION);
        fillListLayout(mListViewIngredients, recipe.getIngredients(), TYPE_INGREDIENT);
        fillListLayout(mListViewTags, recipe.getTags(), TYPE_TAG);
    }

    /**
     * A method that fills a {@link LinearLayout} with data from a List of data.
     * We don't use {@link ListView} since we don't need scrolling, our {@link ScrollView} handles
     * that for us.
     *
     * @param listDirections the layout to fill
     * @param dataset        the data that should be displayed.
     */
    private void fillListLayout(LinearLayout listDirections, List dataset, int type) {
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

    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.activity_recipe_cab, menu);
            return true;
        }

        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu_delete_item_from_list:
                    deleteSelectedItem();
                    mode.finish();
                    return true;
                default:
                    return false;
            }
        }

        // Called when the user exits the action mode
        public void onDestroyActionMode(ActionMode mode) {
            if (selectedCheckBox != null) {
                selectedCheckBox.setChecked(false);
            }
            mActionMode = null;
        }
    };

    /**
     * This deletes an item from our list.
     */
    private void deleteSelectedItem() {
        TaggedItem item = (TaggedItem) selectedCheckBox.getTag();

        switch (item.type) {
            case TYPE_DIRECTION:
                String data = (String) item.data;
                deleteSelectedDirection(data);
                break;
            case TYPE_TAG:
                String tag = (String) item.data;
                deleteSelectedTag(tag);
                break;
            case TYPE_INGREDIENT:
                Ingredient ingredient = (Ingredient) item.data;
                deleteSelectedIngredient(ingredient);
                break;
        }

    }

    private void deleteSelectedDirection(String direction) {
        if (direction != null) {
            mRecipeDirections.remove(direction);
            fillListLayout(mListViewDirections, mRecipeDirections, TYPE_DIRECTION);
        }
    }

    private void deleteSelectedIngredient(Ingredient ingredient) {
        if (ingredient != null) {
            mRecipeIngredients.remove(ingredient);
            fillListLayout(mListViewIngredients, mRecipeIngredients, TYPE_INGREDIENT);
        }
    }

    private void deleteSelectedTag(String tag) {
        if (tag != null) {
            mRecipeTags.remove(tag);
            fillListLayout(mListViewTags, mRecipeTags, TYPE_TAG);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (!isChecked) {
            if (mActionMode != null) {
                // Unchecked, so stop the action mode
                mActionMode.finish();
            }
        } else {
            if (selectedCheckBox != null) {
                // Disable the old checkbox
                selectedCheckBox.setChecked(false);
            }
            selectedCheckBox = buttonView;
            if (mActionMode != null) {
                mActionMode.finish();
            }
            mActionMode = startActionMode(mActionModeCallback);
            mActionMode.setTitle(getResources().getString(R.string.count_items_selected, 1));
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_add_tag:
                addTag();
                break;
            case R.id.button_add_direction:
                addDirection();
                break;
            case R.id.button_add_ingredient:
                addIngredient();
                break;
        }
    }

    /**
     * Add a {@link Ingredient} to the recipe.
     */
    private void addIngredient() {

        if (isEditTextEmpty(mEditTextIngredientName)) {
            // Name should not be empty.
            mEditTextIngredientName.setError(getResources().getString(R.string.blank_field));
            return;
        } else {
            mEditTextIngredientName.setError(null);
        }

        String name = mEditTextIngredientName.getText().toString();
        // Don't parse the float if the field is empty.
        float quantity = isEditTextEmpty(mEditTextIngredientQuantity) ?
                0.0f : Float.parseFloat(mEditTextIngredientQuantity.getText().toString());
        String unit = mEditTextIngredientUnit.getText().toString();
        Ingredient item = new Ingredient(name, unit, quantity);

        //Sanitize the input
        mEditTextIngredientName.setText(null);
        mEditTextIngredientQuantity.setText(null);
        mEditTextIngredientUnit.setText(null);
        mRecipeIngredients.add(item);

        //Update UI
        fillListLayout(mListViewIngredients, mRecipeIngredients, TYPE_INGREDIENT);
    }

    /**
     * Add a Direction to the recipe.
     */
    private void addDirection() {
        if (isEditTextEmpty(mEditTextDirection)) {
            // Field should not be empty
            mEditTextDirection.setError(getResources().getString(R.string.blank_field));
            return;
        } else {
            mEditTextDirection.setError(null);
        }

        String direction = mEditTextDirection.getText().toString();
        mRecipeDirections.add(direction);

        //Sanitize the input
        mEditTextDirection.setText(null);

        //Update UI
        fillListLayout(mListViewDirections, mRecipeDirections, TYPE_DIRECTION);
    }

    /**
     * Add a tag to the recipe.
     */
    private void addTag() {
        if (isEditTextEmpty(mEditTextTag)) {
            // Field should not be empty
            mEditTextTag.setError(getResources().getString(R.string.blank_field));
            return;
        } else {
            mEditTextTag.setError(null);
        }

        String tag = mEditTextTag.getText().toString();
        mRecipeTags.add(tag);
        mRecipeDirections.add(tag);

        //Sanitize the input
        mEditTextTag.setText(null);

        //Update UI
        fillListLayout(mListViewTags, mRecipeTags, TYPE_TAG);
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

    private class ImagePagerAdapter extends PagerAdapter {

        private ArrayList<String> images;

        ImagePagerAdapter(ArrayList<String> images) {
            this.images = images;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((View) object);
        }

        @Override
        public int getCount() {
            return images.size() + 1;
        }

        @Override
        public Object instantiateItem(ViewGroup view, int position) {
            if (position < images.size()) {
                final ImageView imageView = (ImageView) getLayoutInflater().inflate(R.layout.pager_item_recipe_image, view, false);
                ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                imageView.setLayoutParams(lp);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                ImageLoader.getInstance().displayImage(images.get(position), imageView);
                ((ViewPager) view).addView(imageView, 0);
                return imageView;
            } else {
                // Set up an add button
                final ImageButton imageButton = new ImageButton(RecipeActivity.this);
                imageButton.setImageResource(R.drawable.ic_action_add_blue);
                ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                imageButton.setLayoutParams(lp);
                imageButton.setScaleType(ImageView.ScaleType.CENTER);
                imageButton.setOnClickListener(addImageListener);
                ((ViewPager) view).addView(imageButton, 0);
                return imageButton;
            }
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }

        private View.OnClickListener addImageListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(
                        Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        };

        public void swapData(ArrayList<String> images) {
            this.images = images;
            notifyDataSetChanged();
        }
    }

    /**
     * Allows for an image to be selected from the phone's Gallery
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            mRecipePhotos.add("file:///" + picturePath);
            ((ImagePagerAdapter) mViewPhotos.getAdapter()).swapData(mRecipePhotos);
        } else if (requestCode == TAKE_PICTURE && resultCode == RESULT_OK) {
            Uri selectedImage = cameraImageUri;
            mRecipePhotos.add("file:///" + selectedImage);
            ((ImagePagerAdapter) mViewPhotos.getAdapter()).swapData(mRecipePhotos);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.activity_recipe, menu);
        MenuItem actionItem = menu.findItem(R.id.recipe_menu_share);
        mShareActionProvider = (ShareActionProvider) actionItem.getActionProvider();
        mShareActionProvider.setShareHistoryFileName(ShareActionProvider.DEFAULT_SHARE_HISTORY_FILE_NAME);
        if (mRecipe != null) {
            new WriteRecipeToFileTask().execute(mRecipe);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(com.actionbarsherlock.view.MenuItem item) {
        switch (item.getItemId()) {
            case R.id.recipe_menu_camera:
                takePhoto();
                return true;
            case R.id.recipe_menu_save:
                save();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void save() {
        updateRecipeFromUI();
        mController.updateRecipe(mRecipe);
        new WriteRecipeToFileTask().execute(mRecipe);
    }


    private void updateRecipeFromUI() {
        if (mRecipeID == null) {
            mRecipeID = UUID.randomUUID().toString();
        }

        mRecipeName = getEditTextString(mEditTextRecipeName);
        mRecipeDescription = getEditTextString(mEditTextRecipeDescription);

        // We're already keeping track of mRecipeDirections, mRecipeIngredients and mRecipeTags.

        mRecipe = new Recipe(mRecipeID, mRecipeName, mRecipeDescription, null, mRecipeIngredients,
                mRecipeDirections, mRecipePhotos, mRecipeTags);
    }

    private static String getEditTextString(EditText editText) {
        return editText.getText().toString();
    }

    /**
     * Take a photo. Launches the camera app.
     */
    public void takePhoto() {
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photo = new File(Environment.getExternalStorageDirectory(), "Pic.jpg");
        i.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(photo));
        cameraImageUri = Uri.fromFile(photo);
        startActivityForResult(i, TAKE_PICTURE);
    }

    /**
     * Creates a sharing {@link Intent}.
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

    class TaggedItem<T> {
        T data;
        int type;
    }

    private class WriteRecipeToFileTask extends AsyncTask<Recipe, Void, File> {

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
}



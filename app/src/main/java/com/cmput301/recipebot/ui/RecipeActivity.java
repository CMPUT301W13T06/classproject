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
import com.cmput301.recipebot.R;
import com.cmput301.recipebot.model.Ingredient;
import com.cmput301.recipebot.model.Recipe;
import com.nostra13.universalimageloader.core.ImageLoader;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.cmput301.recipebot.util.LogUtils.makeLogTag;

/**
 * An Activity that allows user to add a recipe.
 */
public class RecipeActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {

    private static final String LOGTAG = makeLogTag(RecipeActivity.class);
    public static final String EXTRA_RECIPE = "EXTRA_RECIPE";
    private static final int RESULT_LOAD_IMAGE = 458;
    private static final int TAKE_PICTURE = 531;
    private Uri cameraImageUri;

    @InjectView(R.id.pager_recipe_images)
    ViewPager mRecipePhotos;
    @InjectView(R.id.list_directions)
    LinearLayout mListDirections;
    @InjectView(R.id.list_ingredients)
    LinearLayout mListIngredients;

    @InjectView(R.id.editText_recipe_title)
    EditText mEditTextRecipeTitle;
    @InjectView(R.id.editText_directions)
    EditText mEditTextDirections;
    @InjectView(R.id.button_add_direction)
    ImageButton mButtonAddDirection;
    @InjectView(R.id.editText_ingredients)
    EditText mEditTextIngredients;
    @InjectView(R.id.button_add_ingredient)
    ImageButton mButtonAddIngredient;

    @InjectExtra(EXTRA_RECIPE)
    public Recipe mRecipe;

    protected ActionMode mActionMode;

    CompoundButton selectedCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        fillView();
    }

    /**
     * Fill our Views.
     */
    private void fillView() {
        mRecipePhotos.setAdapter(new ImagePagerAdapter(mRecipe.getPhotos()));
        mEditTextRecipeTitle.setText(mRecipe.getName());
        fillListLayout(mListDirections, mRecipe.getDirections());
        fillListLayout(mListIngredients, mRecipe.getIngredients());
    }

    /**
     * A method that fills a {@link LinearLayout} with data from a List of data.
     *
     * @param listDirections the layout to fill
     * @param data           the data that should be displayed.
     */
    private void fillListLayout(LinearLayout listDirections, List data) {
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //Sanitize the view
        listDirections.removeAllViews();

        for (int i = 0; i < data.size(); i++) {
            Object item = data.get(i);
            CheckBox checkBox = (CheckBox) layoutInflater.inflate(R.layout.checkbox_view, null);
            checkBox.setText(item.toString());
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
     * Uses selected_data, a global variable to know which item to delete.
     */
    private void deleteSelectedItem() {
        Ingredient ingredient = null;
        String direction = null;
        Object data = selectedCheckBox.getTag();
        try {
            ingredient = (Ingredient) data;
        } catch (ClassCastException e1) {
            try {
                direction = (String) data;
            } catch (ClassCastException e2) {
            }
        }

        if (ingredient != null) {
            mRecipe.getIngredients().remove(ingredient);
            fillListLayout(mListIngredients, mRecipe.getIngredients());
        }
        if (direction != null) {
            mRecipe.getDirections().remove(direction);
            fillListLayout(mListDirections, mRecipe.getDirections());
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
        }

    }

    private class ImagePagerAdapter extends PagerAdapter {

        private ArrayList<String> images;
        private LayoutInflater inflater;

        ImagePagerAdapter(ArrayList<String> images) {
            this.images = images;
            this.inflater = getLayoutInflater();
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
            final ImageView imageView = (ImageView) inflater.inflate(R.layout.pager_item_recipe_image, view, false);
            if (position < images.size()) {
                ImageLoader.getInstance().displayImage(images.get(position), imageView);
            } else {
                // Set up an add button
                ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                imageView.setLayoutParams(lp);
                imageView.setImageResource(R.drawable.ic_action_add);
                imageView.setOnClickListener(addImageListener);
            }
            ((ViewPager) view).addView(imageView, 0);
            return imageView;
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
            mRecipe.getPhotos().add("file:///" + picturePath);
            ((ImagePagerAdapter) mRecipePhotos.getAdapter()).swapData(mRecipe.getPhotos());
        } else if (requestCode == TAKE_PICTURE && resultCode == RESULT_OK) {
            Uri selectedImage = cameraImageUri;
            mRecipe.getPhotos().add("file:///" + selectedImage);
            ((ImagePagerAdapter) mRecipePhotos.getAdapter()).swapData(mRecipe.getPhotos());
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.activity_recipe, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(com.actionbarsherlock.view.MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_camera:
                takePhoto();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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

}



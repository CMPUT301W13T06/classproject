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

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.cmput301.recipebot.R;
import com.cmput301.recipebot.model.Ingredient;
import com.cmput301.recipebot.model.Recipe;
import com.cmput301.recipebot.ui.adapters.TextViewListAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;

import java.io.File;
import java.util.ArrayList;

/**
 * An Activity that allows user to add a recipe.
 */
public class RecipeActivity extends BaseActivity {

    public static final String EXTRA_RECIPE = "EXTRA_RECIPE";
    private static final int RESULT_LOAD_IMAGE = 458;
    private static final int TAKE_PICTURE = 531;
    private Uri imageUri;

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

    private TextViewListAdapter mListDirectionsAdapter;
    private TextViewListAdapter mListIngredientsAdapter;

    @InjectExtra(EXTRA_RECIPE)
    public Recipe mRecipe;

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
        mListDirectionsAdapter = new TextViewListAdapter<String>(this, mRecipe.getDirections());
        mListIngredientsAdapter = new TextViewListAdapter<Ingredient>(this, mRecipe.getIngredients());
        mEditTextRecipeTitle.setText(mRecipe.getUser());
        fillListLayout(mListDirections, mListDirectionsAdapter);
        fillListLayout(mListIngredients, mListIngredientsAdapter);
    }

    /**
     * A method that fills a {@link LinearLayout} with data from a {@link TextViewListAdapter}.
     *
     * @param listDirections the layout to fill
     * @param listAdapter    the adapter that provides the data.
     */
    private void fillListLayout(LinearLayout listDirections, TextViewListAdapter listAdapter) {
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View v = listAdapter.getView(i, null, listDirections);
            listDirections.addView(v, i);
        }
    }

    private class ImagePagerAdapter extends PagerAdapter {

        private ArrayList<Uri> images;
        private LayoutInflater inflater;

        ImagePagerAdapter(ArrayList<Uri> images) {
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
                ImageLoader.getInstance().displayImage(images.get(position).toString(), imageView);
            } else {
                // Set up an add button
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

        public void swapData(ArrayList<Uri> images) {
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
            Uri uri = Uri.parse("file:///" + picturePath);
            mRecipe.addPhoto(uri);
            ((ImagePagerAdapter) mRecipePhotos.getAdapter()).swapData(mRecipe.getPhotos());
        } else if (requestCode == TAKE_PICTURE && resultCode == RESULT_OK) {
            Uri selectedImage = imageUri;
            mRecipe.addPhoto(selectedImage);
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
     * Take a photo.
     */
    public void takePhoto() {
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photo = new File(Environment.getExternalStorageDirectory(), "Pic.jpg");
        i.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(photo));
        imageUri = Uri.fromFile(photo);
        startActivityForResult(i, TAKE_PICTURE);
    }

}



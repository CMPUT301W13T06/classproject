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

package com.cmput301.recipebot.ui.adapters;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import com.cmput301.recipebot.R;

import java.util.ArrayList;

/**
 * An extension of {@link ImagePagerAdapter} that shows two additional items.
 * These two items add two extra buttons, an add from gallery button and add from Camera button.
 * It is upto the hostigin activity or fragment to provide a callback for these buttons, and their behaviour is tied
 * to however it is defined by the host.
 */
public class EditableImagePagerAdapter extends ImagePagerAdapter {

    /**
     * Callback for when gallery button is clicked.
     */
    private View.OnClickListener galleryClickListener;

    /**
     * Callback for when camera button is clicked.
     */
    private View.OnClickListener cameraClickListener;

    /**
     * Constructs the adapter.
     *
     * @param context              to make the views
     * @param images               the list of images to display
     * @param galleryClickListener Callback to invoke when gallery button is clicked
     * @param cameraClickListener  Callback to invoke when camera button is clicked.
     */
    public EditableImagePagerAdapter(Context context, ArrayList<String> images,
                                     View.OnClickListener galleryClickListener,
                                     View.OnClickListener cameraClickListener) {
        super(context, images);
        this.galleryClickListener = galleryClickListener;
        this.cameraClickListener = cameraClickListener;
    }

    @Override
    public int getCount() {
        return mImages.size() + 2;
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        if (position < mImages.size()) {
            // Show the imageview
            return super.instantiateItem(view, position);
        }
        final ImageButton imageButton = new ImageButton(mContext);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        imageButton.setLayoutParams(lp);
        imageButton.setScaleType(ImageView.ScaleType.CENTER);
        if (position == mImages.size()) {
            // Set up an add button
            imageButton.setOnClickListener(galleryClickListener);
            imageButton.setImageResource(R.drawable.ic_action_gallery_blue);
        } else if (position == mImages.size() + 1) {
            // Set up a camera button
            imageButton.setOnClickListener(cameraClickListener);
            imageButton.setImageResource(R.drawable.ic_action_camera_blue);
        }
        ((ViewPager) view).addView(imageButton, 0);
        return imageButton;
    }
}

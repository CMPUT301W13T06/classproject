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

public class EditableImagePagerAdapter extends ImagePagerAdapter {

    private View.OnClickListener galleryClickListener;
    private View.OnClickListener cameraClickListener;

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

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
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class ImagePagerAdapter extends PagerAdapter {

    protected ArrayList<String> mImages;
    protected Context mContext;

    public ImagePagerAdapter(Context context, ArrayList<String> images) {
        this.mContext = context;
        this.mImages = images;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }

    @Override
    public int getCount() {
        Log.d("ImagePagerAdapter", mImages.size() + "");
        return mImages.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        final ImageView imageView = new ImageView(mContext);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(lp);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        ImageLoader.getInstance().displayImage(mImages.get(position), imageView);
        ((ViewPager) view).addView(imageView, 0);
        return imageView;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    public void swapData(ArrayList<String> images) {
        this.mImages = images;
        notifyDataSetChanged();
    }
}

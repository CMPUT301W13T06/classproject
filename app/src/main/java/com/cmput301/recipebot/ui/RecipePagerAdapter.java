package com.cmput301.recipebot.ui;

import android.app.Activity;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

public class RecipePagerAdapter extends PagerAdapter {

    private Activity activity;
    private int[] drawableIDs;

    public RecipePagerAdapter(Activity activity,int[] drawableIDs){

        this.activity = activity;
        this.drawableIDs = drawableIDs;
    }

    @Override
    public int getCount() {
        return drawableIDs.length;
    }

    /**
     * Create the page for the given position. The adapter is responsible
     * for adding the view to the container given here, although it only
     * must ensure this is done by the time it returns from
     * {link finup.
     *
     * param container The containing View in which the page will be shown.
     * @param position The page position to be instantiated.
     * @return Returns an Object representing the new page. This does not
     * need to be a View, but can be some other container of the page.
     */
    @Override
    public Object instantiateItem(View collection, int position) {

        ImageView imageView = new ImageView(activity);

        imageView.setBackgroundResource(drawableIDs[position]);

        ((ViewPager) collection).addView(imageView,0);

        return imageView;
    }

    /**
     * Remove a page for the given position. The adapter is responsible
     * for removing the view from its container, although it only must ensure
     * this is done by the time it returns from finup
     */
    @Override
    public void destroyItem(View collection, int position, Object view) {
        ((ViewPager) collection).removeView((ImageView) view);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==((ImageView)object);
    }

    /**
     * Called when the a change in the shown pages has been completed. At this
     * point you must ensure that all of the pages have actually been added or
     * removed from the container as appropriate.
     * param container The containing View which is displaying this adapter's
     * page views.
     */
    @Override
    public void finishUpdate(View arg0) {}

    @Override
    public void restoreState(Parcelable arg0, ClassLoader arg1) {}

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public void startUpdate(View arg0) {}
}
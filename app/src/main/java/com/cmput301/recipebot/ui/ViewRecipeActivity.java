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
import android.widget.CompoundButton;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.cmput301.recipebot.R;
import com.cmput301.recipebot.ui.adapters.ImagePagerAdapter;

/**
 * An activity that allows the user to view a recipe. It displays an actin bar item to allow the user to launch an
 * {@link EditRecipeActivity} to edit the recipe.
 */
public class ViewRecipeActivity extends AbstractRecipeActivity {

    @Override
    public void setPhotos() {
        mViewPhotos.setAdapter(new ImagePagerAdapter(this, mRecipe.getPhotos()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, R.id.menu_edit, Menu.NONE, R.string.menu_edit)
                .setIcon(R.drawable.ic_action_edit)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(com.actionbarsherlock.view.MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_edit:
                editRecipe();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void editRecipe() {
        Intent intent = new Intent(this, EditRecipeActivity.class);
        intent.putExtra(EditRecipeActivity.EXTRA_RECIPE, mRecipe);
        startActivity(intent);
        finish();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        // Don't do anything with this.
    }
}

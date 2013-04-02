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
import android.net.Uri;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.actionbarsherlock.view.Window;
import com.cmput301.recipebot.R;
import com.cmput301.recipebot.model.beans.Recipe;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static com.cmput301.recipebot.util.LogUtils.makeLogTag;

/**
 * An activity that is registered to handle .json files.
 */
public class RecipeReceiverActivity extends BaseActivity {

    private static final String LOGTAG = makeLogTag(RecipeReceiverActivity.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setSupportProgressBarIndeterminateVisibility(true);

        setContentView(new ProgressBar(this));

        Intent receivedIntent = getIntent();
        Uri uri = receivedIntent.getData();

        final Recipe recipe = recipeFromUri(uri);

        if(recipe == null){
            Toast.makeText(this, R.string.invalid_file, Toast.LENGTH_LONG).show();
        } else {
            Intent intent = new Intent(this, ViewRecipeActivity.class);
            intent.putExtra(ViewRecipeActivity.EXTRA_RECIPE, recipe);
            startActivity(intent);
        }
        finish();
    }

    private Recipe recipeFromUri(Uri uri) {
        final String json = fileToJson(uri);
        if(json == null){
            return null;
        }               else {
            Gson gson = new Gson();
            return gson.fromJson(json,Recipe.class);
        }
    }

    private String fileToJson(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            return convertStreamToString(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String convertStreamToString(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        return sb.toString();
    }
}

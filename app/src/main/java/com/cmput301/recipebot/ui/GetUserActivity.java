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
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.cmput301.recipebot.R;
import com.cmput301.recipebot.util.AppConstants;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import roboguice.inject.InjectView;

import static com.cmput301.recipebot.util.LogUtils.makeLogTag;

public class GetUserActivity extends BaseActivity implements View.OnClickListener {

    private static final String LOGTAG = makeLogTag(GetUserActivity.class);

    @InjectView(R.id.user_email)
    EditText mEditTextUserEmail;
    @InjectView(R.id.user_name)
    EditText mEditTextUserName;
    @InjectView(R.id.button_submit)
    Button mButtonSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_user);
        TextView title = (TextView) findViewById(android.R.id.title);
        title.setTypeface(Typeface.createFromAsset(getAssets(), "Roboto-Thin.ttf"));
        mButtonSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        boolean hasError = false;

        if (TextUtils.isEmpty(mEditTextUserEmail.getText())) {
            mEditTextUserEmail.setError(getString(R.string.blank_field));
            hasError = true;
        } else if (!isValidEmail(mEditTextUserEmail.getText())) {
            mEditTextUserEmail.setError(getString(R.string.invalid_email));
            hasError = true;
        } else {
            mEditTextUserEmail.setError(null);
        }

        if (TextUtils.isEmpty(mEditTextUserName.getText())) {
            mEditTextUserName.setError(getString(R.string.blank_field));
            hasError = true;
        } else {
            mEditTextUserName.setError(null);
        }

        if (!hasError) {
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(AppConstants.KEY_USER_EMAIL, mEditTextUserEmail.getText().toString());
            editor.putString(AppConstants.KEY_USER_NAME, mEditTextUserName.getText().toString());
            editor.commit();
            Intent intent = new Intent(GetUserActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        } else {
            Crouton.makeText(this, R.string.invalid, Style.ALERT).show();
        }
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }
}

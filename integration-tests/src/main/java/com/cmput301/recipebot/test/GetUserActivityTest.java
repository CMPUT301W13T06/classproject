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

package com.cmput301.recipebot.test;

import android.app.Instrumentation;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.EditText;
import com.cmput301.recipebot.R;
import com.cmput301.recipebot.ui.GetUserActivity;
import com.cmput301.recipebot.util.AppConstants;
import com.squareup.spoon.Spoon;

import static org.fest.assertions.api.ANDROID.assertThat;
import static org.fest.assertions.api.Assertions.assertThat;

public class GetUserActivityTest extends ActivityInstrumentationTestCase2<GetUserActivity> {

    private GetUserActivity activity;
    private Instrumentation instrumentation;
    private EditText name;
    private EditText email;
    private Button submit;

    /**
     * Create test for {@link com.cmput301.recipebot.ui.MainActivity}
     */
    public GetUserActivityTest() {
        super(GetUserActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        instrumentation = getInstrumentation();
        clearPreferences(instrumentation);
        activity = getActivity();

        name = (EditText) activity.findViewById(R.id.user_name);
        email = (EditText) activity.findViewById(R.id.user_email);
        submit = (Button) activity.findViewById(R.id.button_submit);
    }

    private void clearPreferences(Instrumentation instrumentation) {
        Context context = instrumentation.getTargetContext();
        PreferenceManager.getDefaultSharedPreferences(context).edit().clear().commit();
    }

    public void testEmptyFieldsShowsError() throws Exception {
        Spoon.screenshot(activity, "initial_state");
        IntentFilter filter = new IntentFilter();
        Instrumentation.ActivityMonitor monitor = instrumentation.addMonitor(filter, null, false);

        // Make sure the initial state does not show any errors.
        assertThat(name).hasNoError();
        assertThat(email).hasNoError();

        // Click the "submit" button.
        instrumentation.runOnMainSync(new Runnable() {
            @Override
            public void run() {
                submit.performClick();
            }
        });
        instrumentation.waitForIdleSync();
        Spoon.screenshot(activity, "submit_clicked");

        // Verify errors were shown for both input fields.
        assertThat(email).hasError(R.string.blank_field);
        assertThat(name).hasError(R.string.blank_field);

        // Assert that no new activity was launched.
        assertThat(monitor).hasHits(0);
    }

    public void testEmptyEmailShowsError() throws Exception {
        Spoon.screenshot(activity, "initial_state");
        IntentFilter filter = new IntentFilter();
        Instrumentation.ActivityMonitor monitor = instrumentation.addMonitor(filter, null, false);

        // Make sure the initial state does not show any errors.
        assertThat(name).hasNoError();
        assertThat(email).hasNoError();

        instrumentation.runOnMainSync(new Runnable() {
            @Override
            public void run() {
                name.setText("Prateek");
            }
        });
        instrumentation.waitForIdleSync();
        Spoon.screenshot(activity, "name_entered");

        // Click the "submit" button.
        instrumentation.runOnMainSync(new Runnable() {
            @Override
            public void run() {
                submit.performClick();
            }
        });
        instrumentation.waitForIdleSync();
        Spoon.screenshot(activity, "submit_clicked");

        // Verify errors were shown for both input fields.
        assertThat(email).hasError(R.string.blank_field);
        assertThat(name).hasNoError();

        // Assert that no new activity was launched.
        assertThat(monitor).hasHits(0);
    }

    public void testEmptyNameShowsError() throws Exception {
        Spoon.screenshot(activity, "initial_state");
        IntentFilter filter = new IntentFilter();
        Instrumentation.ActivityMonitor monitor = instrumentation.addMonitor(filter, null, false);

        // Make sure the initial state does not show any errors.
        assertThat(name).hasNoError();
        assertThat(email).hasNoError();

        instrumentation.runOnMainSync(new Runnable() {
            @Override
            public void run() {
                email.setText("f2prateek@gmail.com");
            }
        });
        instrumentation.waitForIdleSync();
        Spoon.screenshot(activity, "email_entered");

        // Click the "submit" button.
        instrumentation.runOnMainSync(new Runnable() {
            @Override
            public void run() {
                submit.performClick();
            }
        });
        instrumentation.waitForIdleSync();
        Spoon.screenshot(activity, "submit_clicked");

        // Verify errors were shown for both input fields.
        assertThat(email).hasNoError();
        assertThat(name).hasError(R.string.blank_field);

        // Assert that no new activity was launched.
        assertThat(monitor).hasHits(0);
    }

    public void testInvalidEmailAndBlankNameShowsBothErrors() throws Exception {
        Spoon.screenshot(activity, "initial_state");
        IntentFilter filter = new IntentFilter();
        Instrumentation.ActivityMonitor monitor = instrumentation.addMonitor(filter, null, false);

        // Make sure the initial state does not show any errors.
        assertThat(name).hasNoError();
        assertThat(email).hasNoError();

        instrumentation.runOnMainSync(new Runnable() {
            @Override
            public void run() {
                email.setText("invalid!");
            }
        });
        instrumentation.waitForIdleSync();
        Spoon.screenshot(activity, "email_entered");

        // Click the "submit" button.
        instrumentation.runOnMainSync(new Runnable() {
            @Override
            public void run() {
                submit.performClick();
            }
        });
        instrumentation.waitForIdleSync();
        Spoon.screenshot(activity, "submit_clicked");

        // Verify errors were shown for both input fields.
        assertThat(email).hasError(R.string.invalid_email);
        assertThat(name).hasError(R.string.blank_field);

        // Assert that no new activity was launched.
        assertThat(monitor).hasHits(0);
    }

    public void testInvalidEmailShowsError() throws Exception {
        Spoon.screenshot(activity, "initial_state");
        IntentFilter filter = new IntentFilter();
        Instrumentation.ActivityMonitor monitor = instrumentation.addMonitor(filter, null, false);

        // Make sure the initial state does not show any errors.
        assertThat(name).hasNoError();
        assertThat(email).hasNoError();

        instrumentation.runOnMainSync(new Runnable() {
            @Override
            public void run() {
                name.setText("Prateek");
            }
        });
        instrumentation.waitForIdleSync();
        Spoon.screenshot(activity, "name_entered");

        instrumentation.runOnMainSync(new Runnable() {
            @Override
            public void run() {
                email.setText("invalid!");
            }
        });
        instrumentation.waitForIdleSync();
        Spoon.screenshot(activity, "email_entered");

        // Click the "submit" button.
        instrumentation.runOnMainSync(new Runnable() {
            @Override
            public void run() {
                submit.performClick();
            }
        });
        instrumentation.waitForIdleSync();
        Spoon.screenshot(activity, "submit_clicked");

        // Verify errors were shown for both input fields.
        assertThat(email).hasError(R.string.invalid_email);
        assertThat(name).hasNoError();

        // Assert that no new activity was launched.
        assertThat(monitor).hasHits(0);
    }

    public void testValidFieldsStartsActivity() throws Exception {
        Spoon.screenshot(activity, "initial_state");

        // Make sure the initial state does not show any errors.
        assertThat(name).hasNoError();
        assertThat(email).hasNoError();

        instrumentation.runOnMainSync(new Runnable() {
            @Override
            public void run() {
                name.setText("Prateek");
            }
        });
        instrumentation.waitForIdleSync();
        Spoon.screenshot(activity, "name_entered");

        instrumentation.runOnMainSync(new Runnable() {
            @Override
            public void run() {
                email.setText("f2prateek@gmail.com");
            }
        });
        instrumentation.waitForIdleSync();
        Spoon.screenshot(activity, "email_entered");

        // Click the "submit" button.
        instrumentation.runOnMainSync(new Runnable() {
            @Override
            public void run() {
                submit.performClick();
            }
        });
        instrumentation.waitForIdleSync();
        Spoon.screenshot(activity, "submit_clicked");

        // Verify errors were shown for both input fields.
        assertThat(email).hasNoError();
        assertThat(name).hasNoError();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(instrumentation.getTargetContext());
        assertThat(sharedPreferences.getString(AppConstants.KEY_USER_EMAIL, null)).isEqualTo("f2prateek@gmail.com");
        assertThat(sharedPreferences.getString(AppConstants.KEY_USER_NAME, null)).isEqualTo("Prateek");
    }

}

/*
 * Copyright 2013 Adam Saturna
 * Copyright 2013 Brian Trinh
 * Copyright 2013 Ethan Mykytiuk
 * Copyright 2013 Prateek Srivastava (@f2prateek)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cmput301.recipebot.ui.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.cmput301.recipebot.R;
import com.cmput301.recipebot.ui.AddPantryItemActivity;
import com.cmput301.recipebot.ui.MainActivity;
import com.github.rtyley.android.sherlock.roboguice.fragment.RoboSherlockListFragment;

import java.util.ArrayList;


/**
 * A fragment that shows a list of items in the pantry.
 */
public class PantryFragment extends RoboSherlockListFragment {

    // id is 0 because the select all has a different id
    int id = 0;
    ArrayList<String> pantry_ingredients_list = new ArrayList<String>();

    // Not sure which one needs to be used. Commented out onCreateView

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setHasOptionsMenu(true);
        fillView();

        Button addButton = (Button) getView().findViewById(R.id.add_button);
        Button searchButton = (Button) getView().findViewById(R.id.search_button);
        CheckBox allChecked = (CheckBox) getView().findViewById(R.id.checkBoxAll);

        // Handles the event when the add button is clicked
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText ingredientsText = (EditText) getView().findViewById(R.id.ingredients_box);
                Log.v("Adding Ingredient", ingredientsText.getText().toString());
                addIngredients(ingredientsText.getText().toString());

            }
        });
        // Handles the event when the search button is clicked
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("Search", "Search Clicked");
                searchRecipes();
            }
        });

        // Handles the event when the select all checkbox is clicked
        allChecked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectAll();
            }
        });
    }
    /*
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_pantry, container, false);

        setHasOptionsMenu(true);
        fillView();


        Button addButton = (Button) v.findViewById(R.id.add_button);
        Button searchButton = (Button) v.findViewById(R.id.search_button);
        CheckBox allChecked = (CheckBox) v.findViewById(R.id.checkBoxAll);

        // Handles the event when the add button is clicked
        addButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                EditText ingredientsText = (EditText) getView().findViewById(R.id.ingredients_box);
                Log.v("Adding Ingredient", ingredientsText.getText().toString());
                addIngredients(ingredientsText.getText().toString());

            }
        });
        // Handles the event when the search button is clicked
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("Search", "Search Clicked");
                searchRecipes();
            }
        });

        // Handles the event when the select all checkbox is clicked
        allChecked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectAll();
            }
        });
        return v;
    }
    */

    // Should take everything from the array list and create checklist the
    private void fillView() {
        for(int i = 0; i < pantry_ingredients_list.size(); i++){
            newCheckBox(pantry_ingredients_list.get(i));
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_pantry, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add_pantry:
                addEntry();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void addEntry() {
        Intent i = new Intent(getSherlockActivity(), AddPantryItemActivity.class);
        startActivity(i);
    }

    // method used to add ingredients
    // need to change to array list once DB is set up
    // TODO: SQL statement adding into database
    public void addIngredients(String ingredients){


        // Test to make sure ingredients it not null
        assert(!ingredients.isEmpty());

        // create new Edit Text object and set it to the one on the screen
        EditText ingredientsText = (EditText) getView().findViewById(R.id.ingredients_box);

        // If the edit text is empty give alert to user
        if(ingredientsText.getText().toString().isEmpty()){
            Log.v("Empty Ingredient", "Yes");

            // AlertDialog.Builder builder = new AlertDialog.Builder(MyActivity.this);
            // Using getActivity instead of myActivity.this
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            builder.setMessage("No item entered. Nothing has been added to your pantry.")
                    .setCancelable(false).setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });

            // Build the alert and show it to the user
            AlertDialog alert = builder.create();
            alert.show();

        } else {

            newCheckBox(ingredients.toUpperCase());

            // ADD INSERT INTO DB
            addToDB();

            // clear the text in the edit text
            ingredientsText.getEditableText().clear();
        }
    }

    // non-functional method for searching
    // add in parameter for the container
    public void searchRecipes(){

        for(int i = 0; i < pantry_ingredients_list.size(); i++){
            CheckBox checker = (CheckBox) getView().findViewById(i);

            // Test to make sure the checkbox is activated
            assert(checker.isActivated());

            // search all recipes that have this ingredient then add the recipe name to an arraylist
            // once list is made then output it
            if(checker.isChecked()){
                // add to list being searched
                Log.v("Checked Off", checker.getText().toString());
            }
        }

        // traverse all of the check boxes
        /* if (is_checked)
            get the text from the check box and add it into database and local cache (depending on how we want to do this)

        SQL SEARCH (Supposed to search the ingredients db (the one holding all recipes) and find matches from the pantry
                    db, then * output all recipes that have at least one of the matching recipes[assumption] *)
            SELECT i.recipe_name
            FROM ingredients i, pantry p
            WHERE i.ingredients = p.description

            go to another activity that shows all the searched?
         */

    }

    // Used to create a new checkbox from the added ingredients and inserts the ingredient into the array list
    // TODO: need to add to DB once that is setup
    public void newCheckBox(String ingredients){

        LinearLayout checkBoxLL = (LinearLayout) getView().findViewById(R.id.checkBox_LinearLayout);

        if(!checkDuplicates(ingredients)) {

            CheckBox cb = new CheckBox(getActivity().getApplicationContext());
            cb.setId(id);
            cb.setText(ingredients);
            pantry_ingredients_list.add(ingredients);
            Log.v("ArrayList", pantry_ingredients_list.get(id));
            checkBoxLL.addView(cb);
            // increase id counter so they have different id
            id++;

        }
    }

    public void selectAll(){

        CheckBox selectAll = (CheckBox) getView().findViewById(R.id.checkBoxAll);

        if(selectAll.isChecked()){
            // Goes through the boxes and sets them to checked
            for(int i = 0; i < pantry_ingredients_list.size(); i++){
                CheckBox checker = (CheckBox) getView().findViewById(i);

                Log.v("ID SET", Integer.toString(checker.getId()));
                Log.v("VALUE OF CHECKBOX", checker.getText().toString());

                checker.setChecked(true);

            }
        } else{
            for(int i = 0; i < pantry_ingredients_list.size(); i++){
                CheckBox checker = (CheckBox) getView().findViewById(i);
                checker.setChecked(false);
            }
        }

    }

    // returns true if the value added is already in the list
    public boolean checkDuplicates(String ingredients){

        for(int i = 0; i < pantry_ingredients_list.size(); i++){

            CheckBox checker = (CheckBox) getView().findViewById(i);
            if(checker.getText().toString().equals(ingredients)){

                // Using getActivity instead of myActivity.this
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                builder.setMessage("Duplicate item. " + checker.getText().toString().toUpperCase()
                        + " is already in your pantry.").setCancelable(false).setPositiveButton
                        ("Okay", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                // Build the alert and show it to the user
                AlertDialog alert = builder.create();
                alert.show();
                Log.v("Value", ingredients);
                return true;
            }
        }
        return false;
    }

    // Stub method for now. Actual search will take place in DB Manager
    public void addToDB(){

    }

}

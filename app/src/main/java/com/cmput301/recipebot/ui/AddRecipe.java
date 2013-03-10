package com.cmput301.recipebot.ui;


//import android.R;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.*;
import com.cmput301.recipebot.R;
import android.app.Activity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageButton;
import android.net.Uri;
import android.database.Cursor;
import android.view.MenuItem;
import android.view.Menu;
import android.view.MotionEvent;

/*
 *     btrinh
 *
 *     For Ethan/Adam
 *     If you guys are looking for the stubs to add your work simply Ctrl+F and search for STUB
 *
 *      Image is currently a place holder. Logic to upload multiple images needs to be figured out.
 *
 *     ToDo:
 *     Horizontal Scroll On Image Button
 *     Multiple Images
 *
 *     Save Title,Author, Date (Not sure how to implement this.. context menu on save?)
 *
 *     Fix "MojoFailureException" somehow.
 *
 *     Completed:
 *     User can publish photo to recipe
 *
 *     Attach Photo to Downloaded Recipe should use logic from previous.
 *
 *     Publish photo/recipe is stubbed and ready for database implementation
 *
 *     Save recipe is stubbed and also ready for cacheing
 *
 *     Share recipe needs menu button fixed.
 *
 *
 */
public class AddRecipe extends Activity {

    private static int RESULT_LOAD_IMAGE = 1;
    private ShareActionProvider mShareActionProvider;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.add_recipe);

        //Buttons
        Button publishButton = (Button) findViewById(R.id.PublishButton);
        Button saveButton = (Button) findViewById(R.id.SaveButton);
        ImageButton imageButton = (ImageButton) findViewById(R.id.ImageButton);

        // Context menu example. Possibly use for save/share if needed
        final CharSequence[] items = {"Red","Green","Blue"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick a color");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                Toast.makeText(getApplicationContext(),items[item], Toast.LENGTH_SHORT).show();

            }
        });
        AlertDialog alert = builder.create();
        alert.show();

        //requestWindowFeature(Window.FEATURE_NO_TITLE);

        /* Allows for Edit Texts to be Scrollable
         *
         * Get the string from these edit texts simply by
         * IngredientEditText.toString();
         * InstructionEditText.toString();
         *
         */
        final EditText IngredientEditText = (EditText) findViewById(R.id.IngredientID);
        IngredientEditText.setMovementMethod(new ScrollingMovementMethod());
        final EditText InstructionEditText = (EditText) findViewById(R.id.InstructionID);
        InstructionEditText.setMovementMethod(new ScrollingMovementMethod());

        /*
        This is the publish button's on click listener.
        The data from this recipe should be uploaded to the database.

        Until menu's share button works this button opens a context menu
        that asks the user whether they wish to publish or share...
        if we intend to keep this way we should change "Publish" to "Share"
         */
        publishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //To change body of implemented methods use File | Settings | File Templates.


                    /*
                    STUB
                     */

            }
        });

         /*
        This is the Save button's on click listener.
        The data from this recipe should be stored for the user to view if needed.
         */
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //To change body of implemented methods use File | Settings | File Templates.
                //onCreateContextMenu();

                    /*
                    STUB
                     */

            }
        });
         /*
        This is the Image button's on click listener.
        A context menu should appear and ask what the user wishes to do with images.
         */
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //To change body of implemented methods use File | Settings | File Templates.
                Intent i = new Intent(
                Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);

                    /*
                    STUB
                     */

            }
        });
    }

    /**
     *  Allows for an image to be selected from the phone's Gallery
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();

            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null,null,null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            ImageButton myImage = (ImageButton) findViewById(R.id.ImageButton);
            myImage.setImageBitmap(BitmapFactory.decodeFile(picturePath));

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu resource file.
        getMenuInflater().inflate(R.menu.recipe_share, menu);

        // Locate MenuItem with ShareActionProvider
        MenuItem item = menu.findItem(R.id.menu_item_share);

        // Fetch and store ShareActionProvider
       // mShareActionProvider.setShareIntent(getShareIntent());
        mShareActionProvider = (ShareActionProvider) item.getActionProvider();

        Intent myShareIntent = new Intent(Intent.ACTION_SEND);
        myShareIntent.setAction(Intent.ACTION_SEND);
        myShareIntent.setType("text/plain");
        myShareIntent.putExtra(Intent.EXTRA_TEXT,"TEST");

        mShareActionProvider.setShareIntent(getShareIntent());

        // myShareIntent.putExtra(Intent.EXTRA_STREAM);

        // myShareIntent.setType("image/jpeg");


        //Return true to display menu
        return true;
    }

    //Call to update the share intent

    private void setShareIntent (Intent shareIntent){
        //if(mShareActionProvider != null){
            mShareActionProvider.setShareIntent(shareIntent);
        //}
    }

    private Intent getShareIntent(){
        Intent myShareIntent = new Intent();
        myShareIntent.setAction(Intent.ACTION_SEND);
        myShareIntent.putExtra(Intent.EXTRA_TEXT,"TEST");

       // myShareIntent.setType("image/jpeg");
        return myShareIntent;
    }

//    parentScrollView.setTouchOnListener(new View.OnTouchListener(){
//            public boolean onTouch(View v, MotionEvent event){
//            Log.v(TAG,"PARENT TOUCH");
//            findViewById(R.id.child_scroll).getParent().requestDisallowInterceptTouchEvent(false);
//            return false;
//        }
//    });

   // }



}

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

package com.cmput301.recipebot.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.lang.ref.WeakReference;

/**
 * A utiltiy class to wrap our bitmap logic in.
 */
public class BitmapUtils {

    /**
     * Encode an image form the given path into a {@link Base64}  string
     *
     * @param imagePath Path of the image to encode (must be absolute)
     * @return a {@link Base64} encoded string.
     */
    public static String encodeBitmap(String imagePath) {
        Bitmap image = BitmapFactory.decodeFile(imagePath);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        byte[] b = bos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
        return imageEncoded;
    }

    /**
     * A task that decodes the an image from a {@link Base64} encoded string into an imageview.
     */
    public static class DecodeBitmapTask extends AsyncTask<String, Void, Bitmap> {
        private final WeakReference<ImageView> imageViewReference;
        private String data = null;

        public DecodeBitmapTask(ImageView imageView) {
            // Use a WeakReference to ensure the ImageView can be garbage collected
            imageViewReference = new WeakReference<ImageView>(imageView);
        }

        // Decode image in background.
        @Override
        protected Bitmap doInBackground(String... params) {
            data = params[0];
            return decodeBitmap(data);
        }

        // Once complete, see if ImageView is still around and set bitmap.
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (imageViewReference != null && bitmap != null) {
                final ImageView imageView = imageViewReference.get();
                if (imageView != null) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        }
    }

    /**
     * Decodes a string into a Bitmap
     *
     * @param input String to decode
     * @return decoded Bitmap
     */
    private static Bitmap decodeBitmap(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

}

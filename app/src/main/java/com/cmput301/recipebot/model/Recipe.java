package com.cmput301.recipebot.model;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Copyright 2013 prateek
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * A recipe class
 */
public class Recipe {

    private int id;
    private String user;
    private String name;
    private ArrayList<Ingredient> ingredients;
    private ArrayList<String> directions;
    private ArrayList<Bitmap> images;

    public Recipe(int id, String user, String name, ArrayList<Ingredient> ingredients,
                  ArrayList<String> directions, ArrayList<Bitmap> images) {
        this.id = id;
        this.user = user;
        this.name = name;
        this.ingredients = ingredients;
        this.directions = directions;
        this.images = images;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public ArrayList<String> getDirections() {
        return directions;
    }

    public void setDirections(ArrayList<String> directions) {
        this.directions = directions;
    }

    public ArrayList<Bitmap> getImages() {
        return images;
    }

    public void setImage(ArrayList<Bitmap> images) {
        this.images = images;
    }

    @Override
    public String toString() {
        return "Recipe [id=" + id + ", user=" + user + ", name=" + name + ", ingredients="
                + ingredients + ", directions=" + directions + "]";
    }

    public static class Builder {
        private int id;
        private String user;
        private String name;
        private ArrayList<Ingredient> ingredients;
        private ArrayList<String> directions;
        private ArrayList<Bitmap> images;

        public Builder setId(int id) {
            this.id = id;
            return this;
        }

        public Builder setUser(String user) {
            this.user = user;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setIngredients(ArrayList<Ingredient> ingredients) {
            this.ingredients = ingredients;
            return this;
        }

        public Builder setDirections(ArrayList<String> directions) {
            this.directions = directions;
            return this;
        }

        public Builder setImages(ArrayList<Bitmap> images) {
            this.images = images;
            return this;
        }

        public Recipe build() {
            return new Recipe(id, user, name, ingredients, directions, images);
        }
    }

}

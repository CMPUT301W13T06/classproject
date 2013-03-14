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

package com.cmput301.recipebot.model;

import android.net.Uri;

import java.util.ArrayList;

/**
 * A recipe class.
 * Setter methods undocumented - use {@link Builder} to build objects of this class.
 */
public class Recipe {

    private int id;
    private String user;
    private String name;
    private ArrayList<Ingredient> ingredients;
    private ArrayList<String> directions;
    private ArrayList<Uri> photos;

    public Recipe(int id, String user, String name, ArrayList<Ingredient> ingredients,
                  ArrayList<String> directions, ArrayList<Uri> photos) {
        this.id = id;
        this.user = user;
        this.name = name;
        this.ingredients = ingredients;
        this.directions = directions;
        this.photos = photos;
    }

    /**
     * Get the unique id for this recipe.
     *
     * @return id of this recipe.
     */
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * Get name of the user who created this recipe.
     *
     * @return Name of user.
     */
    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    /**
     * Get the name of the recipe.
     *
     * @return Name of recipe.
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get ingredients required for this recipe.
     *
     * @return List of ingredients required for this recipe.
     */
    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    /**
     * Get directions for this recipe.
     *
     * @return Ordered set of directions for this recipe.
     */
    public ArrayList<String> getDirections() {
        return directions;
    }

    public void setDirections(ArrayList<String> directions) {
        this.directions = directions;
    }

    /**
     * Get all photoes for this recipe.
     * TODO: consider using a custom Bitmap class, with a boolean unpublished.*
     *
     * @return `photos for this recipe.
     */
    public ArrayList<Uri> getPhotos() {
        return photos;
    }

    public void setPhotos(ArrayList<Uri> photos) {
        this.photos = photos;
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
        private ArrayList<Uri> photos;

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

        public Builder setPhotos(ArrayList<Uri> photos) {
            this.photos = photos;
            return this;
        }

        public Recipe build() {
            return new Recipe(id, user, name, ingredients, directions, photos);
        }
    }

}

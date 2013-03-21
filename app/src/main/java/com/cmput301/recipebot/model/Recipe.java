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

package com.cmput301.recipebot.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * A recipe class.
 */
public class Recipe implements Parcelable {

    private String id;
    private String description;
    private String user;
    private String name;
    private ArrayList<Ingredient> ingredients;
    private ArrayList<String> directions;
    private ArrayList<Uri> photos;

    public Recipe() {
        this.id = null;
        this.description = null;
        this.user = null;
        this.name = null;
        this.directions = new ArrayList<String>();
        this.ingredients = new ArrayList<Ingredient>();
        this.photos = new ArrayList<Uri>();
    }

    public Recipe(String id, String description, String user, String name, ArrayList<Ingredient> ingredients,
                  ArrayList<String> directions, ArrayList<Uri> photos) {
        this.id = id;
        this.description = description;
        this.user = user;
        this.name = name;
        this.ingredients = ingredients;
        this.directions = directions;
        this.photos = photos;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Get the description for this Recipe
     *
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Get the unique id for this recipe.
     *
     * @return id of this recipe.
     */
    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public void addPhoto(Uri photo) {
        photos.add(photo);
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "directions=" + directions +
                ", id='" + id + '\'' +
                ", description='" + description + '\'' +
                ", user='" + user + '\'' +
                ", name='" + name + '\'' +
                ", ingredients=" + ingredients +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(description);
        dest.writeString(user);
        dest.writeString(name);
        dest.writeStringList(directions);
        dest.writeTypedList(ingredients);
        dest.writeTypedList(photos);
    }

    protected Recipe(Parcel in) {
        this();
        id = in.readString();
        description = in.readString();
        user = in.readString();
        name = in.readString();
        in.readStringList(directions);
        in.readTypedList(ingredients, Ingredient.CREATOR);
        in.readTypedList(photos, Uri.CREATOR);
    }

    public static final Parcelable.Creator<Recipe> CREATOR = new Parcelable.Creator<Recipe>() {
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

}
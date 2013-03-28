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

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * A recipe class.
 */
public class Recipe implements Parcelable {

    private String id;
    private String name;
    private String description;
    private User user;
    private ArrayList<Ingredient> ingredients;
    private ArrayList<String> directions;
    private ArrayList<String> photos;
    private ArrayList<String> tags;

    public Recipe(String id, String name, String description, User user, ArrayList<Ingredient> ingredients,
                  ArrayList<String> directions, ArrayList<String> photos, ArrayList<String> tags) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.user = user;
        this.ingredients = ingredients;
        this.directions = directions;
        this.photos = photos;
        this.tags = tags;
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
     * Get name of the user who created this recipe.
     *
     * @return Name of user.
     */
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
     * Get all photos for this recipe.
     *
     * @return `photos for this recipe.
     */
    public ArrayList<String> getPhotos() {
        return photos;
    }

    public void setPhotos(ArrayList<String> photos) {
        this.photos = photos;
    }

    /**
     * Get all tags for the recipe.
     *
     * @return tags for the recipe.
     */
    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "Recipe : " + name + "\n" +
                description + "\n" +
                "by " + user + "\n" +
                "Ingredients : " + ingredients + "\n" +
                "Directions : " + directions + "\n" +
                "Photos : " + photos + "\n" +
                "Tags=" + tags;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeParcelable(user, flags);
        dest.writeStringList(directions);
        dest.writeTypedList(ingredients);
        dest.writeStringList(photos);
        dest.writeStringList(tags);
    }

    protected Recipe(Parcel in) {
        id = in.readString();
        name = in.readString();
        description = in.readString();
        user = in.readParcelable(User.class.getClassLoader());
        directions = new ArrayList<String>();
        in.readStringList(directions);
        ingredients = new ArrayList<Ingredient>();
        in.readTypedList(ingredients, Ingredient.CREATOR);
        photos = new ArrayList<String>();
        in.readStringList(photos);
        tags = new ArrayList<String>();
        in.readStringList(tags);
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
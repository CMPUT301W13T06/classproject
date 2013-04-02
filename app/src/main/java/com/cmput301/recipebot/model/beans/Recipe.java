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

package com.cmput301.recipebot.model.beans;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * A recipe object. This class is the recipe object that is stored on the server and locally.
 */
public class Recipe implements Parcelable {

    /**
     * An ID for this recipe to be identified uniquely. Generated with a random UUID.
     *
     * @see java.util.UUID
     */
    private String id;

    /**
     * The name of the recipe. Must not be null.
     */
    private String name;

    /**
     * A description for this recipe.
     */
    private String description;

    /**
     * The recipes User.
     *
     * @see User
     */
    private User user;

    /**
     * Ingredients required for this recipe. Must contain at least one ingredient.
     */
    private ArrayList<Ingredient> ingredients;

    /**
     * A list of directions to make the recipe. Must contain at least ont direction.
     */
    private ArrayList<String> directions;

    /**
     * A list of photos for the Recipe. Each string can either be a link to :
     * 1. Image on the internet (begins with http:)
     * 2. Local image (begins with file:///)
     * 3. A {@link java.util.prefs.Base64} encoded image.
     */
    private ArrayList<String> photos;

    /**
     * A list of tags describing the recipe. Can be empty.
     */
    private ArrayList<String> tags;

    /**
     * Constructs an empty Recipe object.
     */
    public Recipe() {
        // Default constructor for NewRecipeActivity.
        this.id = null;
        this.name = null;
        this.description = null;
        this.user = null;
        this.ingredients = new ArrayList<Ingredient>();
        this.directions = new ArrayList<String>();
        this.photos = new ArrayList<String>();
        this.tags = new ArrayList<String>();
    }

    /**
     * Constructs a Recipe Object with the given params.
     *
     * @param id          Unique ID of the Recipe.
     * @param name        Name of the Recipe.
     * @param description Description of the Recipe.
     * @param user        User of the Recipe.
     * @param ingredients Ingredients required for the Recipe.
     * @param directions  Directions to make the Recipe.
     * @param photos      Photos of the Recipe.
     * @param tags        Tags of the Recipe.
     */
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
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the id of the recipe.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Get the name of the recipe.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the recipe.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the description of the recipe.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description for this recipe.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Get name of the user who created this recipe.
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets the user of this recipe.
     *
     * @param user
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Get ingredients required for this recipe.
     */
    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    /**
     * Sets the ingredients required for this recipe.
     */
    public void setIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    /**
     * Get directions for this recipe.
     */
    public ArrayList<String> getDirections() {
        return directions;
    }

    /**
     * Sets the directions of this recipe.
     */
    public void setDirections(ArrayList<String> directions) {
        this.directions = directions;
    }

    /**
     * Get all photos for this recipe.
     */
    public ArrayList<String> getPhotos() {
        return photos;
    }

    /**
     * Sets the photos for this recipe.
     */
    public void setPhotos(ArrayList<String> photos) {
        this.photos = photos;
    }

    /**
     * Get all tags for the recipe.
     */
    public ArrayList<String> getTags() {
        return tags;
    }

    /**
     * Set the tags for this recipe.
     */
    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", user=" + user +
                ", ingredients=" + ingredients +
                ", directions=" + directions +
                ", photos=" + photos +
                ", tags=" + tags +
                '}';
    }

    public String toEmail() {
        String ingredientList = "";
        String directionList = "";
        String photoList = "";
        String tagList = "";

        for (Ingredient ingredient : ingredients)
            ingredientList = ingredientList.concat(
                    "\t" + ingredient + "\n");

        for (String direction : directions)
            directionList = directionList.concat(
                    "\t" + direction + "\n");

        for (String tag : tags)
            tagList = tagList.concat(
                    "\t" + tag + "\t\t\n");

        return "Recipe :\n" +
                "Id: " + id + "\n" +
                "Name: " + name + "\n" +
                "Description: " + description + "\n" +
                "User: " + user + "\n" +
                "Ingredients:\n" + ingredientList +
                "Directions:\n" + directionList +
                "Tags: \n" + tagList;
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

    /**
     * Constructs a Recipe from {@link Parcel} object.
     *
     * @param in
     */
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

    /**
     * @see Parcelable.Creator
     */
    public static final Parcelable.Creator<Recipe> CREATOR = new Parcelable.Creator<Recipe>() {
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };
}
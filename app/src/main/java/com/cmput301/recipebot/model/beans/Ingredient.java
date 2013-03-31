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

/**
 * This class stores an Ingredient object. A {@link Recipe} object contains a list of these.
 * It is re-used by {@link com.cmput301.recipebot.model.PantryModel} to save ingredients in the Pantry.
 */
public class Ingredient implements Parcelable {

    /**
     * The name of the ingredient. This is used to match two ingredients.
     */
    private String name;

    /**
     * The quantity for for this ingredient. Could be null.
     */
    private float quantity;

    /**
     * The units that give context to the quantity. e.g. "ml", "nos.", "tbsp", "tsp", etc.
     */
    private String unit;

    /**
     * Construct an Ingredient object.
     *
     * @param name     Name of the ingredient.
     * @param quantity Quantity of the ingredient.
     * @param unit     Units for the quantity of the ingredient.
     */
    public Ingredient(String name, float quantity, String unit) {
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
    }

    /**
     * Returns the name of the ingredient.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the ingredient.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns unit associated with this Ingredient (e.g. 'ml', or 'oz.' or 'nos')
     */
    public String getUnit() {
        return unit;
    }

    /**
     * Sets the unit of the ingredient.
     */
    public void setUnit(String unit) {
        this.unit = unit;
    }

    /**
     * Returns quantity for this ingredient.
     */
    public float getQuantity() {
        return quantity;
    }

    /**
     * Sets the quantity of the ingredient.
     */
    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        if (quantity != 0.0f) {
            if (quantity == (int) quantity) {
                stringBuilder.append(String.format("%d", (int) quantity)).append(" ");
            } else {
                stringBuilder.append(String.format("%s", quantity)).append(" ");
            }
        }

        if (unit != null && !unit.equals("")) {
            stringBuilder.append(unit).append(" ").append("of").append(" ");
        }

        stringBuilder.append(name);
        return stringBuilder.toString();
    }

    @Override
    public boolean equals(Object o) {
        Ingredient ingredient = (Ingredient) o;
        return name.compareToIgnoreCase(ingredient.getName()) == 0;
    }

    /**
     * Constructs an Ingredient object from a {@link Parcel}
     *
     * @param in Parcel to contruct from.
     */
    protected Ingredient(Parcel in) {
        name = in.readString();
        unit = in.readString();
        quantity = in.readFloat();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(unit);
        dest.writeFloat(quantity);
    }

    /**
     * @see Parcelable.Creator
     */
    public static final Parcelable.Creator<Ingredient> CREATOR = new Parcelable.Creator<Ingredient>() {
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };
}

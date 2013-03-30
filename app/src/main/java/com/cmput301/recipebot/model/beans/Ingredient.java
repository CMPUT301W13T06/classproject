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
 * An ingredient class. This is used by the {@link Recipe} object to show all ingredients required.
 */
public class Ingredient implements Parcelable {

    private String name;
    private String unit;
    private float quantity;

    public Ingredient(String name, String unit, float quantity) {
        this.name = name;
        this.unit = unit;
        this.quantity = quantity;
    }

    /**
     * Get the name of the item
     *
     * @return Name of item.
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of the item.
     *
     * @param name Name of item.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns unit associated with this Ingredient (e.g. 'ml', or 'oz.' or 'nos')
     *
     * @return The unit.
     */
    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    /**
     * Returns quantity for this ingredient.
     *
     * @return The quantity required.
     */
    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        if (unit == null || quantity == 0.0f) {
            return name;
        }
        if (quantity == (int) quantity)
            return String.format("%d %s of %s", (int) quantity, unit, name);
        else
            return String.format("%s %s of %s", quantity, unit, name);
    }

    //Parcel code
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

    public static final Parcelable.Creator<Ingredient> CREATOR = new Parcelable.Creator<Ingredient>() {
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };
}

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
 * A user class. Each recipe has a user, who is uniquely identified by their email.
 */
public class User implements Parcelable {

    /**
     * The user's name.
     */
    private String name;

    /**
     * The user's id. For our app, this is just an email.
     */
    private String id;

    /**
     * Constructs a user object.
     *
     * @param id   Email of the user.
     * @param name Name of the user.
     */
    public User(String id, String name) {
        this.id = id;
        this.name = name;

    }

    /**
     * Get the ID of the user.
     */
    public String getId() {
        return id;
    }

    /**
     * Set the ID of the user.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Get the name of the user.
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of the user.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the email of the user. This just returns the ID, since that is the email.
     */
    public String getEmail() {
        return id;
    }

    @Override
    public String toString() {
        return name + " (" + id + ')';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
    }

    /**
     * Constructs a user object from the Parcel.
     *
     * @param in Parcel to construct from.
     */
    protected User(Parcel in) {
        id = in.readString();
        name = in.readString();
    }

    /**
     * @see Parcelable.Creator
     */
    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };

}

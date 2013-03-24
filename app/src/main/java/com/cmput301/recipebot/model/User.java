package com.cmput301.recipebot.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * A user class.
 */
public class User implements Parcelable {

    private String name;
    private String id;

    public User(String id, String name) {
        this.id = id;
        this.name = name;

    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return id;
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

    protected User(Parcel in) {
        id = in.readString();
        name = in.readString();
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };

}

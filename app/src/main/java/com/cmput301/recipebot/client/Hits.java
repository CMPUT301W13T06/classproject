package com.cmput301.recipebot.client;

import java.util.Collection;

public class Hits<T> {
    int total;
    double max_score;
    Collection<ESResponse<T>> hits;

    public Collection<ESResponse<T>> getHits() {
        return hits;
    }

    public String toString() {
        return (super.toString() + "," + total + "," + max_score + "," + hits);
    }
}
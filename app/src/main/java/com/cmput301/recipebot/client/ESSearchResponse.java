package com.cmput301.recipebot.client;

import java.util.ArrayList;
import java.util.Collection;

public class ESSearchResponse<T> {
    int took;
    boolean timed_out;
    transient Object _shards;
    Hits<T> hits;
    boolean exists;

    public Collection<ESResponse<T>> getHits() {
        return hits.getHits();
    }

    public Collection<T> getSources() {
        Collection<T> out = new ArrayList<T>();
        for (ESResponse<T> essrt : getHits()) {
            out.add(essrt.getSource());
        }
        return out;
    }

    public String toString() {
        return (super.toString() + ":" + took + "," + _shards + "," + exists + "," + hits);
    }
}
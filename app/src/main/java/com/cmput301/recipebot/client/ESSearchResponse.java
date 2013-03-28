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
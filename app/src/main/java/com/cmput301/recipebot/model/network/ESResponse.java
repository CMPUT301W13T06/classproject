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

package com.cmput301.recipebot.model.network;

/**
 * A wrapper class for response from ElasticSearch
 * Fields are mappings of a query retrieval from ElasticSearch.
 *
 * @param <T> the data that is wrapped in this response
 */
public class ESResponse<T> {

    String _index;
    String _type;
    String _id;
    int _version;
    boolean exists;
    T _source;
    double max_score;

    /**
     * Get the object associated with this repsonse.
     *
     * @return Object of type T
     */
    public T getSource() {
        return _source;
    }

    @Override
    public String toString() {
        return "Response [id=" + _id;
    }

}

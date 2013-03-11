package com.cmput301.recipebot.client;

/**
 * Copyright 2013 prateek
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class ESResponse<T> {

    String _index;
    String _type;
    String _id;
    int _version;
    boolean exists;
    T _source;
    double max_score;

    public T getSource() {
        return _source;
    }

    @Override
    public String toString() {
        return "Response [id=" + _id;
    }

}

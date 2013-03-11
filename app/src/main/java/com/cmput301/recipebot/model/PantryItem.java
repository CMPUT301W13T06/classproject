/*
 * Copyright 2013 Adam Saturna
 * Copyright 2013 Brian Trinh
 * Copyright 2013 Ethan Mykytiuk
 * Copyright 2013 Prateek Srivastava (@f2prateek)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cmput301.recipebot.model;

/**
 * A class that represents items in the Pantry.
 * For ingredients, use {@link Ingredient}.
 * Match it to an ingredient with its name.
 */
public class PantryItem {

    protected String name;

    public PantryItem(String name) {
        this.name = name;
    }

    /**
     * Get the name of the item
     * @return Name of item.
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of the item.
     * @param name Name of item.
     */
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "PantryItem{" +
                "name='" + name + '\'' +
                '}';
    }
}

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

import com.cmput301.recipebot.model.Recipe;
import com.github.kevinsawicki.http.HttpRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.net.HttpURLConnection;

import static com.cmput301.recipebot.util.LogUtils.makeLogTag;

/**
 * The client class that communicates with our server.
 */
public class ESClient {

    private static final String LOGTAG = makeLogTag(ESClient.class);
    private static final String SERVER_URL = "http://cmput301.softwareprocess.es:8080";
    private static final String CLIENT_INDEX = "test-cmput301w13t06";// "cmput301w13t06";
    private static final String TYPE_RECIPE = "recipe";

    private final Gson gson;

    public ESClient() {
        gson = new Gson();
    }

    /**
     * Get the recipe from the server
     *
     * @param id The id of the recipe to retrieve.
     * @return Recipe with the given id
     */
    public Recipe getRecipe(String id) {
        String response = HttpRequest.get(getRecipeUrl(id)).
                accept("application/json").body();
        Type elasticSearchResponseType = new TypeToken<ESResponse<Recipe>>() {
        }.getType();
        ESResponse<Recipe> esResponse = gson.fromJson(response, elasticSearchResponseType);
        Recipe recipe = esResponse.getSource();
        return recipe;
    }

    /**
     * Insert a recipe to the server
     *
     * @param recipe The recipe to insert.
     * @return True if operation was successful, false otherwise.
     */
    public boolean insertRecipe(Recipe recipe) {
        HttpRequest httpPost = HttpRequest.post(getRecipeUrl(recipe.getId())).send(gson.toJson(recipe));
        return httpPost.code() == HttpURLConnection.HTTP_OK;
    }

    /**
     * Update a recipe to the server.
     * TODO : Actually do an update, right now it just does a force pushe.
     *
     * @param recipe The recipe to insert.
     * @return True if operation was successful, false otherwise.
     */
    public boolean updateRecipe(Recipe recipe) {
        return insertRecipe(recipe);
    }

    /**
     * Delete a recipe from the server
     *
     * @return True if operation was successful, false otherwise.
     */
    public boolean deleteRecipe(String id) {
        HttpRequest httpPost = HttpRequest.delete(getRecipeUrl(id));
        return httpPost.code() == HttpURLConnection.HTTP_OK;
    }

    public static String getRecipeUrl(String id) {
        return SERVER_URL + "/" + CLIENT_INDEX + "/" + TYPE_RECIPE + "/" + id;
    }

}

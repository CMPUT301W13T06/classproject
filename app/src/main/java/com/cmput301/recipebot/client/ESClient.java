package com.cmput301.recipebot.client;

import com.cmput301.recipebot.model.Recipe;
import com.github.kevinsawicki.http.HttpRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.net.HttpURLConnection;

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
public class ESClient {

    private static final String BASE_URL = "http://cmput301.softwareprocess.es:8080";
    private static final String TEAM_URL = "/cmput301w13t06";
    private static final String GET_RECIPE_BASE_URL = BASE_URL + TEAM_URL + "/lab01/%d";

    /**
     * Get the recipe from the server
     *
     * @param id of the recipe to retrieve
     * @return Recipe with the given id
     */
    public static Recipe getRecipe(int id) {
        String response = HttpRequest.get(String.format(GET_RECIPE_BASE_URL, id)).
                accept("application/json").body();
        Gson gson = new Gson();
        Type elasticSearchResponseType = new TypeToken<ESResponse<Recipe>>() {
        }.getType();
        ESResponse<Recipe> esResponse = gson.fromJson(response, elasticSearchResponseType);
        Recipe recipe = esResponse.getSource();
        return recipe;
    }

    /**
     * Insert a recipe to the server
     *
     * @param recipe Recipe to insert
     * @return true if successful
     */
    public static boolean insertRecipe(Recipe recipe) {
        Gson gson = new Gson();
        HttpRequest httpPost = HttpRequest.post(String.format(GET_RECIPE_BASE_URL, recipe.getId())).send(gson.toJson(recipe));
        return httpPost.code() == HttpURLConnection.HTTP_OK;
    }

}

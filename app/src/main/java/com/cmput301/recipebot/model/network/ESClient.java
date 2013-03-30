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

import android.util.Log;
import com.cmput301.recipebot.model.beans.Ingredient;
import com.cmput301.recipebot.model.beans.Recipe;
import com.github.kevinsawicki.http.HttpRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static com.cmput301.recipebot.util.LogUtils.makeLogTag;

/**
 * The network class that communicates with our server.
 */
public class ESClient {

    private static final String LOGTAG = makeLogTag(ESClient.class);
    private static final String SERVER_URL = "http://cmput301.softwareprocess.es:8080";
    private static final String CLIENT_INDEX = "test-cmput301w13t06";// "cmput301w13t06";
    private static final String TYPE_RECIPE = "recipe";
    private static final String METHOD_SEARCH = "_search";

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
        return httpPost.ok();
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
        HttpRequest httpDelete = HttpRequest.delete(getRecipeUrl(id));
        return httpDelete.ok();
    }

    /**
     * Search for all recipes with the given name.
     *
     * @param name The name of the recipe to search for.
     * @return List of recipes that have these ingredients.
     */
    public ArrayList<Recipe> searchRecipes(String name) {
        HttpRequest httpSearch = HttpRequest.get(getRecipeSearchUrl(), true, "q", "name:" + name).accept("application/json");
        return getRecipesFromResponse(httpSearch.body());
    }

    private ArrayList<Recipe> getRecipesFromResponse(String response) {
        Type esSearchResponseType = new TypeToken<ESSearchResponse<Recipe>>() {
        }.getType();
        ESSearchResponse<Recipe> esSearchResponse = gson.fromJson(response, esSearchResponseType);
        ArrayList<Recipe> recipes = new ArrayList<Recipe>();
        for (ESResponse<Recipe> r : esSearchResponse.getHits()) {
            Recipe recipe = r.getSource();
            recipes.add(recipe);
        }
        return recipes;
    }

    /**
     * Search for all recipes with the given Ingredients.
     * It only searches by the name of the ingredient, not its units or quantity.
     *
     * @param ingredients The ingredients to search for.
     * @param matchAll    Whether to only show recipes that have every ingredient.
     * @return List of recipes that have these ingredients.
     */
    public ArrayList<Recipe> searchRecipes(ArrayList<Ingredient> ingredients, boolean matchAll) {
        String operator = matchAll ? " AND " : " OR ";
        String query = "ingredients.name:";
        for (Ingredient i : ingredients) {
            query = query + i.getName() + operator;
        }
        query = query.substring(0, query.length() - operator.length());
        HttpRequest httpSearch = HttpRequest.get(getRecipeSearchUrl(), true, "q", query).accept("application/json");
        return getRecipesFromResponse(httpSearch.body());
    }

    /**
     * Search for all recipes with the given tag.
     *
     * @param tag The tag to search for.
     * @return List of recipes that have these ingredients.
     */
    public ArrayList<Recipe> searchRecipesFromTag(String tag) {
        String query = "tags:" + tag;
        HttpRequest httpSearch = HttpRequest.get(getRecipeSearchUrl(), true, "q", query).accept("application/json");
        return getRecipesFromResponse(httpSearch.body());
    }

    /**
     * Search for all recipes with the given tag.
     *
     * @param username The user to search for.
     * @return List of recipes that have these ingredients.
     */
    public ArrayList<Recipe> searchRecipesFromUser(String username) {
        String query = "user.id:" + username;
        HttpRequest httpSearch = HttpRequest.get(getRecipeSearchUrl(), true, "q", query).accept("application/json");
        Log.d(LOGTAG, httpSearch.toString());
        return getRecipesFromResponse(httpSearch.body());
    }

    /**
     * URL for a specific URL
     *
     * @param id ID whose recipe we need
     * @return the Recipe's url
     */
    public static String getRecipeUrl(String id) {
        return getRecipesUrl() + "/" + id;
    }

    /**
     * Get the Recipe URL
     *
     * @return Recipe URL
     */
    public static String getRecipesUrl() {
        return getIndexUrl() + "/" + TYPE_RECIPE;
    }

    public static String getRecipeSearchUrl() {
        return getRecipesUrl() + "/" + METHOD_SEARCH;
    }

    /**
     * Get the url for our index.
     *
     * @return Index URL
     */
    public static String getIndexUrl() {
        return SERVER_URL + "/" + CLIENT_INDEX;
    }

}

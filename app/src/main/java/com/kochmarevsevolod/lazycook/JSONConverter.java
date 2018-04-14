package com.kochmarevsevolod.lazycook;


import android.widget.ArrayAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class JSONConverter {
    ArrayList<Dish> JSONtoDish(JSONArray arr){
        ArrayList<Dish> dishes = new ArrayList<Dish>();
        try {
            for (int i = 0; i < arr.length(); i++) {
                JSONObject unparsedDish = arr.getJSONObject(i);
                Dish parsedDish = new Dish(unparsedDish.getLong("_id"), unparsedDish.getString("name_dish"), unparsedDish.getString("recipe"),
                        unparsedDish.getInt("salt"), unparsedDish.getInt("stove"), 1);
                dishes.add(parsedDish);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return dishes;
    }
    ArrayList<Ingredient> JSONtoIngredient(JSONArray arr){
        ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
        try {
            for (int i = 0; i < arr.length(); i++) {
                JSONObject unparsedIngredient = arr.getJSONObject(i);
                Ingredient parsedIngredient = new Ingredient(unparsedIngredient.getLong("_id"), unparsedIngredient.getString("name_ingredient"));
                ingredients.add(parsedIngredient);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ingredients;
    }

    ArrayList<Link> JSONtoLink(JSONArray arr){
        ArrayList<Link> links = new ArrayList<Link>();
        try {
            for (int i = 0; i < arr.length(); i++) {
                JSONObject unparsedLink = arr.getJSONObject(i);
                Link parsedLink = new Link(unparsedLink.getLong("_id"), unparsedLink.getLong("id_dish"), unparsedLink.getLong("id_ingredient"),
                        unparsedLink.getInt("amount"), unparsedLink.getString("unit"));
                links.add(parsedLink);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return links;
    }

    int[] JSONtoMinMax(JSONArray arr){
        int[] minmax = new int[2];
        try {
            JSONObject unparsedMinMax = arr.getJSONObject(0);
            String s = unparsedMinMax.getString("minmax");
            int min = Integer.valueOf(s.substring(0, s.indexOf(",")));
            int max = Integer.valueOf(s.substring(s.indexOf(",")+1));
            minmax[0] = min;
            minmax[1] = max;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return minmax;
    }

    ArrayList<DishWithIngs> IngStrToStr(JSONArray arr){

        ArrayList<DishWithIngs> dwis = new ArrayList<DishWithIngs>();
        try {
            for (int i = 0; i < arr.length(); i++) {
                JSONObject unparsedDwi = arr.getJSONObject(i);
                DishWithIngs parsedDwi = new DishWithIngs(unparsedDwi.getString("res").substring(0,unparsedDwi.getString("res").indexOf(",")),
                        unparsedDwi.getString("res").substring(unparsedDwi.getString("res").indexOf(",")+1));
                dwis.add(parsedDwi);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return dwis;
    }

    String RecipeToStr(JSONArray arr){
        String res = new String();
        try {
            res = arr.getJSONObject(0).getString("recipe");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return res;
    }

    String StatToStr(JSONArray arr){
        int res1 = 0;
        int res2 = 0;
        myLog.v("values for stat = " + arr);
        String res = new String();
        try {
            res1 = arr.getJSONObject(0).getInt("numdishes");
            res2 = arr.getJSONObject(0).getInt("numingrs");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        res = "В данный момент на сервере содержится информация о " + res1 + " блюдах, и используется " + res2 + " различных ингредиентов";
        myLog.v("stat = " + res);
        return res;
    }

}

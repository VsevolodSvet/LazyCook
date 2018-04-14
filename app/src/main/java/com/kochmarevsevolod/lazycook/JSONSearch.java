package com.kochmarevsevolod.lazycook;

import org.json.JSONArray;

public interface JSONSearch {
    void fill_results_list(JSONArray result);
    void get_dish_ingredients(JSONArray result);
    void set_dish_recipe(JSONArray result);
    void set_statistics(JSONArray result);
}

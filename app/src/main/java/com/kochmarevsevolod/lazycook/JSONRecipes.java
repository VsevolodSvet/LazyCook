package com.kochmarevsevolod.lazycook;

import org.json.JSONArray;

public interface JSONRecipes {
    void get_dishes(JSONArray result);
    void get_filtered_dishes(JSONArray result);
    void save_dish(JSONArray result);
    void save_ingredients(JSONArray result);
    void save_links(JSONArray result);
}

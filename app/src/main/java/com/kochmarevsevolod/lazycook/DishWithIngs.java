package com.kochmarevsevolod.lazycook;

public class DishWithIngs {
    private String dish_name;
    private String ingredients;

    DishWithIngs(String d, String i){
        dish_name = d;
        ingredients = i;
    }

    String get_dish_name(){
        return dish_name;
    }

    String getIngredients(){
        return ingredients;
    }

    void setIngredients(String ings){
        ingredients = ings;
    }
}

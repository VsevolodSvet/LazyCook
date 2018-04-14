package com.kochmarevsevolod.lazycook;

public class Dish {
  private long id;
  private String name;
  private String recipe;
  private int salt;
  private int stove;
  private int favorite;

  private static int default_salt = 0;
  private static int default_stove = 0;
  private static int default_favorite = 0;

  // Блок методов задания значений параметров

  public void setId(long id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setRecipe(String recipe) {
    this.recipe = recipe;
  }

  public void setSalt(int salt) {
    this.salt = salt;
  }

  public void setStove(int stove) {
    this.stove = stove;
  }
  
  public void setFavorite(int favorite) {
    this.favorite = favorite;
  }

  public Dish(long id, String name, String recipe, int salt, int stove, int favorite) {
    this.id = id;
    this.name = name;
    this.recipe = recipe;
    this.salt = salt;
    this.stove = stove;
    this.favorite = favorite;
  }

  public Dish(long id, String name, String recipe) {
    this.id = id;
    this.name = name;
    this.recipe = recipe;
    this.salt = default_salt;
    this.stove = default_stove;
    this.favorite = default_favorite;
  }

  public void setDefaults(int def_salt, int def_stove, int def_favorite) {
    default_salt = def_salt;
    default_stove = def_stove;
    default_favorite = def_favorite;
  }

  // Блок методов получения значений параметров

  public long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getRecipe() {
    return recipe;
  }

  public int getSalt() {
    return salt;
  }

  public int getStove() {
    return stove;
  }

  public int getFavorite() {
    return favorite;
  }

  public int[] getDefaults() {
    return new int[] { default_salt, default_stove, default_favorite };
  }  

  public String[] getAll() {
    return new String[] { ""+id, name, recipe, ""+salt, ""+stove, ""+favorite };
  }

  @Override
  public String toString() {
    return name/* + "\n\n" + recipe*/;
  }
} 
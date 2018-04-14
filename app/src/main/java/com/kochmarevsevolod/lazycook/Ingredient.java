package com.kochmarevsevolod.lazycook;

public class Ingredient {
  private long id;
  private String name;

  // Блок методов задания значений параметров

  public void setId(long id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Ingredient(long id, String name) {
    this.id = id;
    this.name = name;
  }

  // Блок методов получения значений параметров

  public long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String[] getAll() {
    return new String[] { ""+id, name };
  }

  @Override
  public String toString() {
    return name;
  }
} 
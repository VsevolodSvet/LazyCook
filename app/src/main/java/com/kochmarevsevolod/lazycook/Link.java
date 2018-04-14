package com.kochmarevsevolod.lazycook;

public class Link {
  private long id;
  private long id_dish;
  private long id_ingredient;
  private int amount;
  private String unit;

  // Блок методов задания значений параметров

  public void setId(long id) {
    this.id = id;
  }

  public void setIdDish(long id_dish) {
    this.id_dish = id_dish;
  }

  public void setIdIngredient(long id_ingredient) {
    this.id_ingredient = id_ingredient;
  }

  public void setAmount(int amount) {
    this.amount = amount;
  }

  public void setUnit(String unit) {
    this.unit = unit;
  }

  public Link(long id, long id_dish, long id_ingredient, int amount, String unit) {
    this.id = id;
    this.id_dish = id_dish;
    this.id_ingredient = id_ingredient;
    this.amount = amount;
    this.unit = unit;
  }


  // Блок методов получения значений параметров

  public long getId() {
    return id;
  }

  public long getIdDish() {
    return id_dish;
  }

  public long getIdIngredient() {
    return id_ingredient;
  }

  public int getAmount() {
    return amount;
  }

  public String getUnit() {
    return unit;
  }

  public String[] getAll() {
    return new String[] { ""+id, ""+id_dish, ""+id_ingredient, ""+amount, unit };
  }

  @Override
  public String toString() {
    return unit;
  }
} 
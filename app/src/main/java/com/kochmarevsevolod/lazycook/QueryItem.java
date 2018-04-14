package com.kochmarevsevolod.lazycook;

public class QueryItem{
	private long id_ingredient;
	private String unit;
	private int amount;

    public void setId(long id){
        id_ingredient = id;
    }

    public void setUnit(String u){
        unit = u;
    }

    public void setAmount(int a){
        amount = a;
    }

    public long getId(){
        return id_ingredient;
    }

    public String getUnit(){
        return unit;
    }

    public int getAmount(){
        return amount;
    }

}

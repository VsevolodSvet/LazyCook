package com.kochmarevsevolod.lazycook;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DishDataSource {

    // Database fields
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.COLUMN_NAME_DISH, MySQLiteHelper.COLUMN_RECIPE,
            MySQLiteHelper.COLUMN_SALT, MySQLiteHelper.COLUMN_STOVE,
            MySQLiteHelper.COLUMN_FAVORITE};

    public DishDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Dish AddDish(String dish, String recipe, int salt, int stove, int favorite) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_NAME_DISH, dish);
		values.put(MySQLiteHelper.COLUMN_RECIPE, recipe);
		values.put(MySQLiteHelper.COLUMN_SALT, salt);
		values.put(MySQLiteHelper.COLUMN_STOVE, stove);
		values.put(MySQLiteHelper.COLUMN_FAVORITE, favorite);
        long insertAll = database.insert(MySQLiteHelper.TABLE_DISH, null,
                values);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_DISH,
                allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertAll , null,
                null, null, null);
        //database.execSQL("INSERT into lazycook."+MySQLiteHelper.TABLE_DISH+" Values ('"+dish+"', '"+recipe+"', "+salt+", "+stove+", 1)");
        cursor.moveToFirst();
        Dish newDish = cursorToDish(cursor);
        cursor.close();
        return newDish;
    }

    public void deleteDish(Dish dish) {
        long id = dish.getId();
        database.delete(MySQLiteHelper.TABLE_DISH, MySQLiteHelper.COLUMN_ID
                + " = " + id, null);
    }

    public ArrayList<Dish> getAllDishes() {
        ArrayList<Dish> dishes = new ArrayList<Dish>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_DISH,
                allColumns, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Dish dish = cursorToDish(cursor);
            dishes.add(dish);
            cursor.moveToNext();
        }
        cursor.close();
        return dishes;
    }

    public ArrayList<Dish> getAllFavDishes() {
        ArrayList<Dish> dishes = new ArrayList<Dish>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_DISH,
                allColumns, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Dish dish = cursorToDish(cursor);
            if (dish.getFavorite() == 1) dishes.add(dish);
            cursor.moveToNext();
        }
        cursor.close();
        return dishes;
    }

    public ArrayList<Dish> findAllDishes(String filter) {
        ArrayList<Dish> dishes = new ArrayList<Dish>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_DISH,
                allColumns, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Dish dish = cursorToDish(cursor);
            if (dish.getName().contains(filter)){
                dishes.add(dish);
            }
            cursor.moveToNext();
        }
        cursor.close();
        return dishes;
    }

    public boolean checkDishByID(long id){
        Cursor cursor = database.query(MySQLiteHelper.TABLE_DISH,
                allColumns, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Dish dish = cursorToDish(cursor);
            if (dish.getId() == id){
                return true;
            }
            cursor.moveToNext();
        }
        return false;
    }

    public boolean checkDishByName(String str){
        Cursor cursor = database.query(MySQLiteHelper.TABLE_DISH,
                allColumns, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Dish dish = cursorToDish(cursor);
            if (dish.getName().equals(str)){
                return true;
            }
            cursor.moveToNext();
        }
        return false;
    }

    private Dish cursorToDish(Cursor cursor) {
        Dish dish = new Dish(-1, null, null, -1, -1, -1);
        dish.setId(cursor.getLong(0));
        dish.setName(cursor.getString(1));
		dish.setRecipe(cursor.getString(2));
		dish.setSalt(cursor.getInt(3));
		dish.setStove(cursor.getInt(4));
		dish.setFavorite(cursor.getInt(5));
        return dish;
    }

    public void AddDish(Dish d) {
        database.execSQL("INSERT into "+MySQLiteHelper.TABLE_DISH+" Values ("+d.getId()+",'"+d.getName()+"', '"+d.getRecipe()+"', "+d.getSalt()+", "+d.getStove()+", 1)");
    }
}

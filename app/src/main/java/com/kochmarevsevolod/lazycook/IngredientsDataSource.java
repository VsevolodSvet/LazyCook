package com.kochmarevsevolod.lazycook;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class IngredientsDataSource {

    // Database fields
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.COLUMN_NAME_INGREDIENT };

    public IngredientsDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Ingredient AddIngredient(String ingredient) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_NAME_INGREDIENT, ingredient);
        long insertName = database.insert(MySQLiteHelper.TABLE_INGR, null,
                values);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_INGR,
                allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertName, null,
                null, null, null);
        cursor.moveToFirst();
        Ingredient newIngredient = cursorToIngredient(cursor);
        cursor.close();
        return newIngredient;
    }

    public void AddIngredient(Ingredient i) {
        database.execSQL("INSERT into "+MySQLiteHelper.TABLE_INGR+" Values ("+i.getId()+",'"+i.getName()+"')");
    }

    public void deleteIngredient(Ingredient ingredient) {
        long id = ingredient.getId();
        database.delete(MySQLiteHelper.TABLE_INGR, MySQLiteHelper.COLUMN_ID
                + " = " + id, null);
    }

    public boolean checkIngByName(String str){
        Cursor cursor = database.query(MySQLiteHelper.TABLE_INGR,
                allColumns, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Ingredient ingredient = cursorToIngredient(cursor);
            if (ingredient.getName() == str){
                return true;
            }
            cursor.moveToNext();
        }
        return false;
    }

    public List<Ingredient> getAllIngredients() {
        List<Ingredient> ingredients = new ArrayList<Ingredient>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_INGR,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Ingredient ingredient = cursorToIngredient(cursor);
            ingredients.add(ingredient);
            cursor.moveToNext();
        }
        cursor.close();
        return ingredients;
    }

    public ArrayList<Ingredient> findAllIngredients(String filter) {
        ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_INGR,
                allColumns, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Ingredient ingredient = cursorToIngredient(cursor);
            if (ingredient.getName().contains(filter)){
                ingredients.add(ingredient);
            }
            cursor.moveToNext();
        }
        cursor.close();
        return ingredients;
    }

    private Ingredient cursorToIngredient(Cursor cursor) {
        Ingredient ingredient = new Ingredient(-1, null);
        ingredient.setId(cursor.getLong(0));
        ingredient.setName(cursor.getString(1));
        return ingredient;
    }
}

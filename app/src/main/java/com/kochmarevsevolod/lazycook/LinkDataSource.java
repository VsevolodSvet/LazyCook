package com.kochmarevsevolod.lazycook;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class LinkDataSource {

    // Database fields
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.COLUMN_ID_DISH,
            MySQLiteHelper.COLUMN_ID_INGREDIENT,
            MySQLiteHelper.COLUMN_AMOUNT,
            MySQLiteHelper.COLUMN_UNIT};

    public LinkDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Link AddLink(long id_dish, long id_ingredient, int amount, String unit) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_ID_DISH, id_dish);
		values.put(MySQLiteHelper.COLUMN_ID_INGREDIENT, id_ingredient);
		values.put(MySQLiteHelper.COLUMN_AMOUNT, amount);
		values.put(MySQLiteHelper.COLUMN_UNIT, unit);
        long insertAll = database.insert(MySQLiteHelper.TABLE_LINK, null,
                values);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_LINK,
                allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertAll , null,
                null, null, null);
        cursor.moveToFirst();
        Link newLink = cursorToLink(cursor);
        cursor.close();
        return newLink;
    }

    public void AddLink(Link l) {
        database.execSQL("INSERT into "+MySQLiteHelper.TABLE_LINK+" Values ("+l.getId()+","+l.getIdDish()+","+l.getIdIngredient()+","+l.getAmount()+",'"+l.getUnit()+"')");
    }

    public void deleteLink(Link link) {
        long id = link.getId();
        database.delete(MySQLiteHelper.TABLE_LINK, MySQLiteHelper.COLUMN_ID
                + " = " + id, null);
    }

    public List<Link> getAllLinks() {
        final List<Link> links = new ArrayList<Link>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_LINK,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Link link = cursorToLink(cursor);
            links.add(link);
            cursor.moveToNext();
        }
        cursor.close();
        return links;
    }

    public List<Link> findAllLinksByIngredientId(long id) {
        final List<Link> links = new ArrayList<Link>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_LINK,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Link link = cursorToLink(cursor);
            long l = link.getIdIngredient();
            if(l == id) {
                links.add(link);
            }
            cursor.moveToNext();
        }
        cursor.close();
        return links;
    }

    public List<String> findAllLinkNamesByIngredientId(long id) {
        List<String> links = new ArrayList<String>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_LINK,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Link link = cursorToLink(cursor);
            if(link.getIdIngredient() == id) {
                links.add(link.getUnit());
            }
            cursor.moveToNext();
        }
        cursor.close();
        return links;
    }

    private Link cursorToLink(Cursor cursor) {
        Link link = new Link(-1, -1, -1, -1, null);
        link.setId(cursor.getLong(0));
        link.setIdDish(cursor.getLong(1));
        link.setIdIngredient(cursor.getLong(2));
        link.setAmount(cursor.getInt(3));
        link.setUnit(cursor.getString(4));
        return link;
    }
}

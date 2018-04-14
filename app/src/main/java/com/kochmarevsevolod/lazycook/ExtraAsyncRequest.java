package com.kochmarevsevolod.lazycook;

import android.os.AsyncTask;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.ArrayAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Logger;

import android.util.Log.*;

public final class ExtraAsyncRequest extends AsyncTask<String, Void, JSONArray> {

    final static String MSSQL_DB = "jdbc:jtds:sqlserver://10.0.3.2:1433:/Lazycook;";
    final static String MSSQL_LOGIN = "lazyuser";
    final static String MSSQL_PASS= "12344321";
    public final static JSONConverter conv = new JSONConverter();
    public String action;

    private JSONSearch mySearch;

    public ExtraAsyncRequest(JSONSearch Interface){
        this.mySearch = Interface;
    }


    @Override
    protected JSONArray doInBackground(String[] query) {
        JSONArray resultSet = new JSONArray();
        myLog.v("starting request...");
        action = query[1];
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            Connection con = null;
            Statement st = null;
            ResultSet rs = null;
            try {
                con = DriverManager.getConnection(MSSQL_DB, MSSQL_LOGIN, MSSQL_PASS);
                if (con != null) {
                    st = con.createStatement();
                    rs = st.executeQuery(query[0]);
                    if (rs != null) {
                        int columnCount = rs.getMetaData().getColumnCount();
                        // Сохранение данных в JSONArray
                        while (rs.next()) {
                            JSONObject rowObject = new JSONObject();
                            for (int i = 1; i <= columnCount; i++) {
                                rowObject.put(rs.getMetaData().getColumnName(i), (rs.getString(i) != null) ? rs.getString(i) : "");
                            }
                            resultSet.put(rowObject);
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (rs != null) rs.close();
                    if (st != null) st.close();
                    if (con != null) con.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e.getMessage());
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        myLog.v("result set = "+resultSet);
        return resultSet;
    }

    @Override
    protected void onPostExecute(JSONArray result) {
        switch (action){
            case "get_dish_ingredients" : {
                if (mySearch != null && result != null) mySearch.get_dish_ingredients(result);
                break;
            }
            case "get_dish_recipe" : {
                if (mySearch != null && result != null) mySearch.set_dish_recipe(result);
                break;
            }
            case "get_statistics" : {
                if (mySearch != null && result != null) mySearch.set_statistics(result);
                break;
            }
        }
    }
}
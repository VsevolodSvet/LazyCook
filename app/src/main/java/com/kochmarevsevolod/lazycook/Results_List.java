package com.kochmarevsevolod.lazycook;

import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Results_List extends AppCompatActivity implements JSONSearch {

    public ArrayList<DishWithIngs> groups = new ArrayList<DishWithIngs>();
    ExpandableListView list;
    SimpleExpandableListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_result);

        String[] query = new String[2];
        query[1] = "get_statistics";
        query[0] = "SELECT * FROM dbo." + query[1] + "()";

        new ExtraAsyncRequest(Results_List.this).execute(query);

        String[] mquery = new String[2];
        mquery[1] = "find_dishes";
        mquery[0] = "EXEC " + mquery[1] + " " + Search_Page.salt + ", " + Search_Page.stove;
        for (int i = 0; i < Search_Page.QueryList.size() && i < 14; i++){
            mquery[0] = mquery[0] + ", " + Search_Page.QueryList.get(i).getId() + ", \'" + Search_Page.QueryList.get(i).getUnit() +
                    "\', " + Search_Page.QueryList.get(i).getAmount();
        }

        list  = (ExpandableListView) findViewById(R.id.reslist);

        list.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int itemClicked) {
                String[] query = new String[2];
                query[1] = "get_dish_recipe";
                query[0] = "SELECT dbo." + query[1] + "(\'" +
                        adapter.getGroup(itemClicked).toString().substring(10, adapter.getGroup(itemClicked).toString().length() - 1) + "\') as recipe";
                myLog.v("onclick_query = " + query[0]);
                new ExtraAsyncRequest(Results_List.this).execute(query);
                return;
            }
        });

        list.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int itemClicked) {
                TextView txt = (TextView) findViewById(R.id.textView);
                txt.setText("");
                return;
            }
        });
        new AsyncRequest(Results_List.this).execute(mquery);
    }

    @Override
    public void fill_results_list(JSONArray result) {
        JSONConverter conv = new JSONConverter();
        List<Dish> values = conv.JSONtoDish(result);
        String queryhelp = new String();
        String[] query = new String[2];
        query[0] = "";
        for (int i = 0; i < values.size(); i++) {
            query[1] = "get_dish_ingredients";
            query[0] = query[0] + "DECLARE @o" + i + " varchar(255); EXEC " + query[1] + " " + values.get(i).getId() + ", @o" + i +" OUTPUT; ";
            queryhelp = queryhelp + "SELECT @o" + i + " as res UNION ";
        }
        if (queryhelp.length() > 7) query[0] = query[0] + queryhelp.substring(0, queryhelp.length() - 7);
        myLog.v("queeeery = " + query[0]);
        new ExtraAsyncRequest(Results_List.this).execute(query);
    }

    @Override
    public void get_dish_ingredients(JSONArray result) {
        myLog.v("res = " + result);
        JSONConverter conv = new JSONConverter();
        ArrayList<DishWithIngs> dwi = conv.IngStrToStr(result);
        for (int i = 0; i < dwi.size(); i++) {
            groups.add(dwi.get(i));
        }
        setExpandableListView();
    }

    public void setExpandableListView(){
        Map<String, String> map;
        ArrayList<Map<String, String>> groupDataList = new ArrayList<>();
        ArrayList<DishWithIngs> values = groups;

        for (int i = 0; i < values.size(); i++) {
            map = new HashMap<>();
            map.put("dishName", values.get(i).get_dish_name());
            myLog.v("values.get("+i+").get_dish_name()" + values.get(i).get_dish_name());
            groupDataList.add(map);
        }
        String groupFrom[] = new String[] { "dishName" };
        int groupTo[] = new int[] { android.R.id.text1 };
        ArrayList<ArrayList<Map<String, String>>> сhildDataList = new ArrayList<>();
        for (int i = 0; i < values.size(); i++) {
            ArrayList<Map<String, String>> сhildDataItemList = new ArrayList<>();
            map = new HashMap<>();
            map.put("dishIngredients", values.get(i).getIngredients());
            сhildDataItemList.add(map);
            сhildDataList.add(сhildDataItemList);
        }

        String childFrom[] = new String[] { "dishIngredients" };
        int childTo[] = new int[] { android.R.id.text1 };

        adapter = new SimpleExpandableListAdapter(
                this, groupDataList,
                android.R.layout.simple_expandable_list_item_1, groupFrom,
                groupTo, сhildDataList, android.R.layout.simple_list_item_1,
                childFrom, childTo);

        if (adapter != null) list.setAdapter(adapter);
        else {
            TextView txt = (TextView)findViewById(R.id.textView);
            //txt.setText("НИЧЕГО НЕ НАЙДЕНО!");
        }
        list.deferNotifyDataSetChanged();
    }

    @Override
    public void set_dish_recipe(JSONArray result) {
        TextView txt = (TextView)findViewById(R.id.textView);
        JSONConverter conv = new JSONConverter();
        if (conv.RecipeToStr(result) != "") txt.setText(conv.RecipeToStr(result));
        else txt.setText("К сожалению, для этого блюда пока нет рецепта");
    }

    @Override
    public void set_statistics(JSONArray result) {
        TextView stat = (TextView) findViewById(R.id.statistics);
        JSONConverter conv = new JSONConverter();
        if (stat.getText() == null || stat.getText() == "") stat.setText(conv.StatToStr(result));
    }
}
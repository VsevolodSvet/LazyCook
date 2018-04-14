package com.kochmarevsevolod.lazycook;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Recipes_List extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, JSONRecipes {

    DishDataSource fav_dishes = new DishDataSource(this);
    IngredientsDataSource fav_ings = new IngredientsDataSource(this);
    LinkDataSource fav_links = new LinkDataSource(this);

    int currentNumOfDishes = 0;
    int neededNumOfDishes = 50;
    int scrollcount = 0;
    int scroll = 1;

    myLog Log;

    ArrayList<Dish> global_dishes = new ArrayList<Dish>();
    ArrayList<Dish> saved_global_dishes = new ArrayList<Dish>();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        final EditText filter = (EditText) findViewById(R.id.recipeFilter);
        filter.setText("");

        Button search = (Button) findViewById(R.id.search_button);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!filter.getText().toString().equals("")) {
                    if (saved_global_dishes.size() < currentNumOfDishes){
                        saved_global_dishes.clear();
                        saved_global_dishes.addAll(global_dishes);
                    }
                    global_dishes = new ArrayList<Dish>();
                    String[] newquery = new String[2];
                    newquery[1] = "get_filtered_dishes";
                    newquery[0] = "SELECT * FROM Dishes WHERE CHARINDEX('" + filter.getText().toString() + "',name_dish) > 0";
                    new AsyncRequest(Recipes_List.this).execute(newquery);
                    Log.v("I work");
                }
                else {
                    global_dishes.clear();
                    global_dishes.addAll(saved_global_dishes);
                    setExpandableListView(global_dishes);
                    Log.v("I work (empty)");
                }
            }
        });
        String[] query = new String[2];
        query[1] = "get_dishes";
        query[0] = "SELECT * FROM Dishes WHERE _id > " + currentNumOfDishes + " AND _id <= " + neededNumOfDishes;
        new AsyncRequest(Recipes_List.this).execute(query);
        currentNumOfDishes = 50;
        neededNumOfDishes += 20;
    }

    public void setExpandableListView(ArrayList<Dish> val){
        Map<String, String> map;
        ArrayList<Map<String, String>> groupDataList = new ArrayList<>();
        final ArrayList<Dish> values = val;

        for (int i = 0; i < values.size(); i++) {
            map = new HashMap<>();
            map.put("dishName", values.get(i).getName());
            groupDataList.add(map);
        }
        String groupFrom[] = new String[] { "dishName" };
        int groupTo[] = new int[] { R.id.label };
        ArrayList<ArrayList<Map<String, String>>> сhildDataList = new ArrayList<>();
        for (int i = 0; i < values.size(); i++) {
            ArrayList<Map<String, String>> сhildDataItemList = new ArrayList<>();
            map = new HashMap<>();
            map.put("dishRecipe", values.get(i).getRecipe());
            сhildDataItemList.add(map);
            сhildDataList.add(сhildDataItemList);
        }

        String childFrom[] = new String[] { "dishRecipe" };
        int childTo[] = new int[] { android.R.id.text1 };

        final SimpleExpandableListAdapter adapter = new SimpleExpandableListAdapter(
                this, groupDataList,
                R.layout.fav_adapter, groupFrom,
                groupTo, сhildDataList, android.R.layout.simple_list_item_1,
                childFrom, childTo);

        final ExpandableListView list = (ExpandableListView) findViewById(R.id.list_recipes);
        adapter.notifyDataSetChanged();
        list.setAdapter(adapter);
        list.deferNotifyDataSetChanged();
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                fav_dishes.open();
                String s = list.getExpandableListAdapter().getGroup(position).toString();
                s = s.substring(10,s.length()-1);
                if (fav_dishes.checkDishByName(s)){
                    Toast toast = Toast.makeText(getApplicationContext(),"Такой рецепт уже есть в избранном", Toast.LENGTH_LONG);
                    toast.show();
                    fav_dishes.close();
                }
                else {
                    fav_dishes.close();
                    String[] query1 = new String[2];
                    String[] query2 = new String[2];
                    String[] query3 = new String[2];
                    query1[1] = "save_dish";
                    query1[0] = "SELECT * FROM Dishes WHERE name_dish = '"+s+"'";
                    Log.v("query1 = "+query1[0]);
                    query2[1] = "save_ingredients";
                    query2[0] = "SELECT Ingredients._id, name_ingredient FROM Ingredients JOIN DishIngredient \n" +
                            "ON Ingredients._id = DishIngredient.id_ingredient \n" +
                            "WHERE id_dish = (SELECT max(_id) FROM Dishes WHERE name_dish = '"+s+"')";
                    Log.v("query2 = "+query2[0]);
                    query3[1] = "save_links";
                    query3[0] = "SELECT * FROM DishIngredient " +
                            "WHERE id_dish = (SELECT max(_id) FROM Dishes WHERE name_dish = '"+s+"')";
                    Log.v("query3 = "+query3[0]);
                    new AsyncRequest(Recipes_List.this).execute(query1);
                    new AsyncRequest(Recipes_List.this).execute(query2);
                    new AsyncRequest(Recipes_List.this).execute(query3);
                }
                return true;
            }
        });
        list.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem + visibleItemCount == currentNumOfDishes){
                    String[] query = new String[2];
                    query[1] = "get_dishes";
                    query[0] = "SELECT * FROM Dishes WHERE _id > " + currentNumOfDishes + " AND _id <= " + neededNumOfDishes;
                    new AsyncRequest(Recipes_List.this).execute(query);
                    scroll = currentNumOfDishes;
                    scrollcount = visibleItemCount;
                    currentNumOfDishes = neededNumOfDishes;
                    neededNumOfDishes += 20;
                }
            }
        });
        if (scroll > 1){
            list.setSelectedGroup(scroll-scrollcount);
        }
        else {
            for (int i = 0; i < currentNumOfDishes; i++){

            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.recipes_page) {
            Intent intent = new Intent(Recipes_List.this, Recipes_List.class);
            startActivity(intent);
            this.finish();
        } else if (id == R.id.search_page) {
            Intent intent = new Intent(Recipes_List.this, Search_Page.class);
            startActivity(intent);
            this.finish();
        } else if (id == R.id.favorites_page) {
			Intent intent = new Intent(Recipes_List.this, Favorites_List.class);
            startActivity(intent);
            this.finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void get_dishes(JSONArray result) {
        JSONConverter conv = new JSONConverter();
        saved_global_dishes.addAll(conv.JSONtoDish(result));
        global_dishes.addAll(conv.JSONtoDish(result));
        setExpandableListView(global_dishes);
    }

    @Override
    public void get_filtered_dishes(JSONArray result) {
        JSONConverter conv = new JSONConverter();
        ArrayList<Dish> global_dish_search = new ArrayList<Dish>();
        global_dish_search.addAll(conv.JSONtoDish(result));
        setExpandableListView(global_dish_search);
    }

    @Override
    public void save_dish(JSONArray result){
        JSONConverter jc = new JSONConverter();
        List<Dish> dish = jc.JSONtoDish(result);
        fav_dishes.open();
        fav_dishes.AddDish(dish.get(0));
        fav_dishes.close();
    }

    @Override
    public void save_ingredients(JSONArray result){
        JSONConverter js = new JSONConverter();
        List<Ingredient> li = js.JSONtoIngredient(result);
        fav_ings.open();
        for (int i = 0; i < li.size(); i++){
            if (fav_ings.checkIngByName(li.get(i).getName())) fav_ings.AddIngredient(li.get(i));
        }
        fav_ings.close();
    }

    @Override
    public void save_links(JSONArray result){
        JSONConverter js = new JSONConverter();
        fav_links.open();
        List<Link> ll = js.JSONtoLink(result);
        for (int i = 0; i < ll.size(); i++){
            fav_links.AddLink(ll.get(i));
        }
        fav_links.close();
        Toast toast = Toast.makeText(getApplicationContext(), "Рецепт добавлен!", Toast.LENGTH_LONG);
        toast.show();
    }
}

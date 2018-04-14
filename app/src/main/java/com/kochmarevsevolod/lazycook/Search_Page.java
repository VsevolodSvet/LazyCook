package com.kochmarevsevolod.lazycook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class Search_Page extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private IngredientsDataSource datasource;

	private List<Ingredient> values_ch;
	public static List<QueryItem> QueryList = new ArrayList<QueryItem>();

    public static long id_intent;

    public static int salt = 0;
    public static int stove = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        final ListView list_ing = (ListView) findViewById(R.id.list_ingredients);
        final ListView list_ch = (ListView) findViewById(R.id.list_chosen);

        Button search_button = (Button) findViewById(R.id.search);
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Search_Page.this, Results_List.class);
                startActivity(intent);
            }
        });

        final CheckBox Salt = (CheckBox) findViewById(R.id.salt);
        Salt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Salt.isChecked()) salt = 1;
                else salt = 0;
            }
        });

        final CheckBox Stove = (CheckBox) findViewById(R.id.stove);
        Stove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Stove.isChecked()) stove = 1;
                else stove = 0;
            }
        });


        datasource = new IngredientsDataSource(this);
        datasource.open();

        final List<Ingredient> values_ing = datasource.getAllIngredients();
        /*
		final List<Ingredient>*/ values_ch = new ArrayList<Ingredient>();

        final ArrayAdapter<Ingredient> adapter_ing = new ArrayAdapter<Ingredient>(this, android.R.layout.simple_list_item_1, values_ing);
        list_ing.setAdapter(adapter_ing);

        final ArrayAdapter<Ingredient> adapter_ch = new ArrayAdapter<Ingredient>(this, android.R.layout.simple_list_item_1, values_ch);
        list_ch.setAdapter(adapter_ch);

        list_ing.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View itemClicked, int position, long id) {

                final Ingredient item = values_ing.get((int) id);
                list_ing.post(new Runnable() {
                    @Override
                    public void run() {
                        final Intent intent;
                        intent = new Intent(Search_Page.this, UA.class);
                        id_intent = item.getId();
                        startActivity(intent);
                        values_ing.remove(item);
                        adapter_ing.notifyDataSetChanged();
                    }
                });

                list_ch.post(new Runnable() {
                    @Override
                    public void run() {
                        values_ch.add(item);
                        adapter_ch.notifyDataSetChanged();
                    }
                });
            }
        });

        list_ch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View itemClicked, int position, long id) {
                final Ingredient item = values_ch.get((int) id);
                list_ch.post(new Runnable() {
                    @Override
                    public void run() {
                        values_ch.remove(item);
                        QueryList.remove(item.getId());
                        adapter_ch.notifyDataSetChanged();
                    }
                });

                list_ing.post(new Runnable() {
                    @Override
                    public void run() {
                        values_ing.add(item);
                        adapter_ing.notifyDataSetChanged();
                    }
                });
            }
        });
    }

	/*
		
	public void OnSearchClick(){
		

	}
		
	*/
	
    public void onClick(View view) {

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
            Intent intent = new Intent(Search_Page.this, Recipes_List.class);
            startActivity(intent);
            this.finish();
        } else if (id == R.id.search_page) {
            Intent intent = new Intent(Search_Page.this, Search_Page.class);
            startActivity(intent);
            this.finish();
        } else if (id == R.id.favorites_page) {
			Intent intent = new Intent(Search_Page.this, Favorites_List.class);
            startActivity(intent);
            this.finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}




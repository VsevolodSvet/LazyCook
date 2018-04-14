package com.kochmarevsevolod.lazycook;

import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;

import java.util.List;

public class UA extends AppCompatActivity implements JSONInterface{

	private QueryItem Item = new QueryItem();
	private int current_min = 0;
	Spinner spinner;

	@Override
    protected void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ua);
		Button BtnOk = (Button) findViewById(R.id.ok);
		BtnOk.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final Spinner spinner = (Spinner)findViewById(R.id.spinner);
				Item.setUnit(spinner.getSelectedItem().toString());
				final TextView amount_text = (TextView) findViewById(R.id.amount);
				Item.setAmount(Integer.valueOf((String) amount_text.getText()));
				Search_Page.QueryList.add(Item);
				finish();
			}
		});
		Item.setId(Search_Page.id_intent);
		Search_Page.id_intent = 0;
		String[] query = new String[2];
		query[1] = "get_ingredient_links";
		query[0] = "EXEC " + query[1] + " " + Item.getId();
		new AsyncRequest(UA.this).execute(query);
		/*datasource = new LinkDataSource(this);
		datasource.open();
		List<Link> links = datasource.findAllLinksByIngredientId(Item.getId());
		datasource.close();*/ //
		final TextView amount_text = (TextView) findViewById(R.id.amount);
		amount_text.setText("0");
		spinner = (Spinner) findViewById(R.id.spinner);

		final SeekBar seekbar = (SeekBar) findViewById(R.id.seekbar);
		seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				int real_progress = progress + current_min;
				amount_text.setText("" + real_progress);
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {

			}
		});
		/*seekbar.setMax();
		seekbar.setOnSeekBarChangeListener(String.valueOf(seekbar.getProgress()));*/
	}

	@Override
	public void fill_spinner(JSONArray result){
		final Spinner spinner = (Spinner)findViewById(R.id.spinner);
		JSONConverter conv = new JSONConverter();
		List<Link> links = conv.JSONtoLink(result);
		ArrayAdapter<Link> possible_units = new ArrayAdapter<Link>(this, android.R.layout.simple_list_item_1, links);
		spinner.setAdapter(possible_units);
		spinner.setSelection(0);
		possible_units.notifyDataSetChanged();
		String[] query = new String[2];
		query[1] = "get_min_and_max_amount";
		query[0] = "SELECT dbo." + query[1] + "(\'" + spinner.getSelectedItem().toString() + "\') as minmax";
		myLog.v("query = "+query[0]);
		new AsyncRequest(UA.this).execute(query);
	}

	@Override
	public void set_seekbar_parameters(JSONArray result){
		JSONConverter conv = new JSONConverter();
		int [] minmax = conv.JSONtoMinMax(result);
		final SeekBar seekBar = (SeekBar)findViewById(R.id.seekbar);
		current_min = minmax[0];
		seekBar.setMax(minmax[1]-current_min);
		final TextView amount_text = (TextView) findViewById(R.id.amount);
		amount_text.setText("" + minmax[0]);
	}

}
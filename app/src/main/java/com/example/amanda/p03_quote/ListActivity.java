package com.example.amanda.p03_quote;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {
	String[] cv_nameArray = new String [100];
	String [] cv_priceArray = new String [100];
	String [] cv_changeArray = new String [100];
	String  [] cv_percentArray = new String [100];
	ListActivity cv_this = this;
	DBHelper cv_db;
	SwipeRefreshLayout cv_swipeLayout;
	int cv_currentStocks = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);

		//database
		cv_db = new DBHelper(this);
		cv_db.dbf_initRows();

		//array list, adapter, and popup dialog
		final ArrayList<ListData> lv_listItems = cv_db.dbf_getAllRecords();
		final ListAdapter lv_adapter = new ListAdapter(this, lv_listItems);
		final AlertDialog.Builder cv_alert = new AlertDialog.Builder(this);

		//views
		final TextView cv_stockNum = (TextView)findViewById(R.id.xv_stockNum);
		ListView cv_listView = (ListView) findViewById(R.id.xv_tabletList);
		cv_listView.setAdapter(lv_adapter);

		// pull and assign data
		for (int i = 0; i<lv_listItems.size(); i++) {
			String lv_url = "http://download.finance.yahoo.com/d/quotes.csv?s=" + lv_listItems.get(i).getSymbol() + "&f=snl1t1c1p2baophgkj&e=.csv";
			String lv_strVal = cfp_downloadHttp(lv_url);
			final String[] lv_tokens = lv_strVal.replace("\"", "").split(",");
			cv_nameArray[i] = lv_tokens[1];
			if (lv_tokens[2].equals(" Inc.")){
				cv_priceArray[i] = lv_tokens[3].substring(0, lv_tokens[3].indexOf(".") + 3);
				cv_changeArray[i] = lv_tokens[5].substring(0, lv_tokens[5].indexOf(".") + 3);
				cv_percentArray[i] = lv_tokens[6].substring(0, lv_tokens[6].indexOf(".") + 3);
				continue;
			}
			cv_priceArray[i] = lv_tokens[2].substring(0, lv_tokens[2].indexOf(".") + 3);
			cv_changeArray[i] = lv_tokens[4].substring(0, lv_tokens[4].indexOf(".") + 3);
			cv_percentArray[i] = lv_tokens[5].substring(0, lv_tokens[5].indexOf(".") + 3);
		}
		for (int i = 0; i < lv_listItems.size(); i++) {
			lv_listItems.set(i, new ListData(lv_listItems.get(i).getSymbol(),
					cv_nameArray[i], cv_priceArray[i],
					cv_changeArray[i], cv_percentArray[i]));
		}
		lv_adapter.notifyDataSetChanged();
		cv_currentStocks = lv_listItems.size();
		cv_stockNum.setText(String.valueOf(cv_currentStocks));

		//goto detail activity on click
		cv_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent cv_intent = new Intent(cv_this, DetailedActivity.class);
				//pass symbol to sub
				cv_intent.putExtra("symbol", lv_listItems.get(position).getSymbol());
				startActivity(cv_intent);
				overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
			}
		});

		//delete on long click
		cv_listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
			                               int position, long id) {
				cv_db.dbf_deletePart(lv_listItems.get(position).getSymbol());
				lv_listItems.remove(position);
				lv_adapter.notifyDataSetChanged();
				cv_currentStocks = lv_listItems.size();
				cv_stockNum.setText(String.valueOf(cv_currentStocks));
				return false;
			}
		});

		//add stock with FAB button
		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.xv_fab);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				final EditText edittext = new EditText(cv_this);
				cv_alert.setMessage("Enter Stock Symbol");
				cv_alert.setTitle("Add Quote Info");
				cv_alert.setView(edittext);
				cv_alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						String addSymbol = edittext.getText().toString().toUpperCase();
						if (!addSymbol.equals("")) {
							cv_db.dbf_appendSymbol(addSymbol);
							Intent intent = getIntent();
							finish();
							startActivity(intent);
						}
					}
				});
				cv_alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
					}
				});
				cv_alert.show();
			}
		});

		//set refresh listener
		cv_swipeLayout = (SwipeRefreshLayout) findViewById(R.id.xv_swipeLayout);
		cv_swipeLayout.setColorSchemeColors(Color.BLUE);

		cv_swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				cv_swipeLayout.setRefreshing(true);
				for (int i = 0; i<lv_listItems.size(); i++) {
					String lv_url = "http://download.finance.yahoo.com/d/quotes.csv?s=" + lv_listItems.get(i).getSymbol() + "&f=snl1t1c1p2baophgkj&e=.csv";
					String lv_strVal = cfp_downloadHttp(lv_url);
					final String[] lv_tokens = lv_strVal.replace("\"", "").split(",");
					cv_nameArray[i] = lv_tokens[1];
					if (lv_tokens[2].equals(" Inc.")){
						cv_priceArray[i] = lv_tokens[3].substring(0, lv_tokens[3].indexOf(".") + 3);
						cv_changeArray[i] = lv_tokens[5].substring(0, lv_tokens[5].indexOf(".") + 3);
						cv_percentArray[i] = lv_tokens[6].substring(0, lv_tokens[6].indexOf(".") + 3);
						continue;
					}
					cv_priceArray[i] = lv_tokens[2].substring(0, lv_tokens[2].indexOf(".") + 3);
					cv_changeArray[i] = lv_tokens[4].substring(0, lv_tokens[4].indexOf(".") + 3);
					cv_percentArray[i] = lv_tokens[5].substring(0, lv_tokens[5].indexOf(".") + 3);
				}
				for (int i = 0; i < lv_listItems.size(); i++) {
					lv_listItems.set(i, new ListData(lv_listItems.get(i).getSymbol(),
							cv_nameArray[i], cv_priceArray[i],
							cv_changeArray[i], cv_percentArray[i]));
				}
				lv_adapter.notifyDataSetChanged();
				cv_currentStocks = lv_listItems.size();
				cv_stockNum.setText(String.valueOf(cv_currentStocks));
				cv_swipeLayout.setRefreshing(false);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private String cfp_downloadHttp(String url) {
		String lv_str = "";

		StrictMode.ThreadPolicy policy = new StrictMode.
				ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);

		try {
			URL lv_url = new URL(url);
			HttpURLConnection lv_con = (HttpURLConnection) lv_url.openConnection();
			lv_str = cfp_readStream(lv_con.getInputStream());

		} catch (Exception e) {
			e.printStackTrace();
			lv_str = "Connection error: " + e.toString();
		}
		return lv_str;
	}

	private String cfp_readStream(InputStream in) {
		BufferedReader lv_reader = null;
		StringBuilder lv_sb = new StringBuilder();
		try {
			lv_reader = new BufferedReader(new InputStreamReader(in));
			String nextLine = "";
			while ((nextLine = lv_reader.readLine()) != null) {
				lv_sb.append(nextLine);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (lv_reader != null) {
				try {
					lv_reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return lv_sb.toString();
		}
	}
}

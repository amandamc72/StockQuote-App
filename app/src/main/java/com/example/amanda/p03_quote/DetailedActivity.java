package com.example.amanda.p03_quote;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class DetailedActivity extends AppCompatActivity implements View.OnClickListener {
	Timer cv_timer;
	MyTimerTask cv_myTimerTask;
	ImageButton cxv_playButton;
	ImageButton cxv_refreshButton;
	ImageButton cxv_pauseButton;
	MyCanvas cv_myCanvas;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);

		cxv_pauseButton = (ImageButton) findViewById(R.id.xv_pauseButton);
		cxv_playButton = (ImageButton) findViewById(R.id.xv_playButton);
		cxv_refreshButton = (ImageButton) findViewById(R.id.xv_refreshButton);

		cxv_pauseButton.setOnClickListener(this);
		cxv_playButton.setOnClickListener(this);
		cxv_refreshButton.setOnClickListener(this);
		cv_myCanvas = (MyCanvas) findViewById(R.id.xv_canvas);

		cxv_playButton.performClick();
	}

	public String symbol (){
		if (getIntent() != null){
			Bundle extras = getIntent().getExtras();
			String symbol = extras != null ? extras.getString("symbol"):"";
			return symbol;
		}
		return null;
	}

	public void onClick(View v) {

		if (v.getId() == R.id.xv_playButton) {
			if (cv_timer != null) {
				cv_timer.cancel();
			}
			cv_timer = new Timer();
			cv_myTimerTask = new MyTimerTask();
			cv_timer.schedule(cv_myTimerTask, 0, 1000);
		} else if (v.getId() == R.id.xv_pauseButton) {
			cv_timer.cancel();
		} else if (v.getId() == R.id.xv_refreshButton) {
			cv_timer.cancel();
			cv_myTimerTask.run();
		}
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


	//Nav Back to list
	@Override
	public void onBackPressed() {
		cfp_navBack();
	}

	private void cfp_navBack (){
		Intent lv_it = new Intent(this, ListActivity.class);
		startActivity(lv_it);
		overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_list, menu);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle("Back");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//back button as menuitem, no need of onCreateOptionsMenu(Menu menu)
		if (id == android.R.id.home) {
			cfp_navBack();
		}
		return super.onOptionsItemSelected(item);
	}


	class MyTimerTask extends TimerTask {
		@Override
		public void run() {
			final TextView lv_labelSymbol = (TextView) findViewById(R.id.xv_symbol);
			final TextView lv_labelName = (TextView) findViewById(R.id.xv_companyName);
			final TextView lv_labelLastTrade = (TextView) findViewById(R.id.xv_lastTradeVal);
			final TextView lv_labelTime = (TextView) findViewById(R.id.xv_timeVal);
			final TextView lv_labelChange = (TextView) findViewById(R.id.xv_changeVal);
			final TextView lv_labelPercentChange = (TextView) findViewById(R.id.xv_changePercentVal);
			final TextView lv_labelBid = (TextView) findViewById(R.id.xv_bidVal);
			final TextView lv_labelAsk = (TextView) findViewById(R.id.xv_askVal);
			final TextView lv_labelOpen = (TextView) findViewById(R.id.xv_openVal);
			final TextView lv_labelPreClose = (TextView) findViewById(R.id.xv_precloseVal);
			final TextView lv_labelHigh = (TextView) findViewById(R.id.xv_highVal);
			final TextView lv_labelLow = (TextView) findViewById(R.id.xv_lowVal);
			final TextView lv_label52High = (TextView) findViewById(R.id.xv_52wHighVal);
			final TextView lv_label52Low = (TextView) findViewById(R.id.xv_52wlowVal);

			String lv_symbol = symbol();
			String lv_url = "http://download.finance.yahoo.com/d/quotes.csv?s=" + lv_symbol + "&f=snl1t1c1p2baophgkj&e=.csv";
			String lv_strVal = cfp_downloadHttp(lv_url);
			final String[] lv_tempArr = lv_strVal.replace("\"", "").split(",");
			final ArrayList<String> lv_results = new ArrayList<>();
			for (int i = 0; i<lv_tempArr.length; i++)
				lv_results.add(lv_tempArr[i]);

			if (lv_results.size() >= 15){
				lv_results.remove(2);
			}

			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					lv_labelSymbol.setText(lv_results.get(0));
					lv_labelName.setText(lv_results.get(1));
					lv_labelLastTrade.setText(lv_results.get(2).substring(0, lv_results.get(2).indexOf(".") + 3));
					lv_labelTime.setText(lv_results.get(3));
					lv_labelChange.setText(lv_results.get(4).substring(0, lv_results.get(4).indexOf(".") + 3));
					lv_labelPercentChange.setText(" " + lv_results.get(5).substring(0, lv_results.get(5).indexOf(".") + 3) + "%");
					lv_labelBid.setText(lv_results.get(6).substring(0, lv_results.get(6).indexOf(".") + 3) + " x 800");
					lv_labelAsk.setText(lv_results.get(7).substring(0, lv_results.get(7).indexOf(".") + 3) + " x 100");
					lv_labelOpen.setText(lv_results.get(8).substring(0, lv_results.get(8).indexOf(".") + 3));
					lv_labelPreClose.setText(lv_results.get(9).substring(0, lv_results.get(9).indexOf(".") + 3));
					lv_labelHigh.setText(lv_results.get(10).substring(0, lv_results.get(10).indexOf(".") + 3));
					lv_labelLow.setText(lv_results.get(11).substring(0, lv_results.get(11).indexOf(".") + 3));
					lv_label52High.setText(lv_results.get(12).substring(0, lv_results.get(12).indexOf(".") + 3));
					lv_label52Low.setText(lv_results.get(13).substring(0, lv_results.get(13).indexOf(".") + 3));

					float lv_myNum = 0;
					try {
						lv_myNum = Float.parseFloat(lv_results.get(4));
						if (lv_myNum >= 0.00) {
							//green
							lv_labelChange.setBackgroundResource(R.drawable.rounded_corner_green);
						} else {
							//red
							lv_labelChange.setBackgroundResource(R.drawable.rounded_corner_red);
						}
					} catch (NumberFormatException nfe) {

					}
				}
			});
		}
	}
}



package com.example.amanda.p03_quote;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ListAdapter extends BaseAdapter {
	private Context c2v_context;
	private ArrayList<ListData> c2v_listItems;

	public ListAdapter(Context context, ArrayList<ListData> listItems){
		this.c2v_context = context;
		this.c2v_listItems = listItems;
	}

	@Override
	public int getCount() {
		return c2v_listItems.size();
	}

	@Override
	public Object getItem(int position) {
		return c2v_listItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater mInflater = (LayoutInflater)
					c2v_context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(R.layout.my_list, null);
		}

		TextView lv_symbolLbl = (TextView) convertView.findViewById(R.id.xv2_symbolLbl);
		lv_symbolLbl.setText(c2v_listItems.get(position).getSymbol());

		TextView lv_nameLbl = (TextView) convertView.findViewById(R.id.xv2_nameLbl);
		lv_nameLbl.setText(c2v_listItems.get(position).getName());

		TextView lv_priceLbl = (TextView) convertView.findViewById(R.id.xv2_priceLbl);
		lv_priceLbl.setText(c2v_listItems.get(position).getPrice());

		TextView lv_changeLbl = (TextView) convertView.findViewById(R.id.xv2_changeLbl);
		lv_changeLbl.setText(c2v_listItems.get(position).getChange());

		TextView lv_percentLbl = (TextView) convertView.findViewById(R.id.xv2_percentLbl);
		lv_percentLbl.setText(" " + c2v_listItems.get(position).getPercent()+"%  ");

		float lv_myNum = 0;
		try {
			lv_myNum = Float.parseFloat(lv_changeLbl.getText().toString());
			if (lv_myNum >= 0.00) {
				//green
				lv_changeLbl.setBackgroundResource(R.drawable.rounded_corner_green);
			} else {
				//red
				lv_changeLbl.setBackgroundResource(R.drawable.rounded_corner_red);
			}
		} catch (NumberFormatException nfe) {

		}

		return convertView;
	}

}


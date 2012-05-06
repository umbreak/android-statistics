package com.chart.browser.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chart.R;
import com.chart.pojos.ChartCategory;
//Adapter for the AllContentsFragment
public class CategoriesContentsAdapter extends ArrayAdapter<ChartCategory>{
	private final LayoutInflater mInflater;
	private final Context context;

	public CategoriesContentsAdapter(Context context) {
		super(context, R.layout.list_item_categories);

		this.context=context;
		mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}


	public void setData(List<ChartCategory> data) {
		clear();
		if (data != null) {
			for (ChartCategory category : data) {
				add(category);
			}
		}
	}

	/**
	 * Populate new items in the list.
	 */
	@Override public View getView(int position, View convertView, ViewGroup parent) {
		View view;

		if (convertView == null) {
			view = mInflater.inflate(R.layout.list_item_categories, parent, false);
		} else {
			view = convertView;
		}

		//Recover ChartCategory Data and fill the list_item_categories layout with it.
		ChartCategory item = getItem(position);

		((TextView) view.findViewById(android.R.id.text1)).setText(item.name);

		final ImageView iconView = (ImageView) view.findViewById(R.id.imageView1);
		iconView.setImageResource(getFlagResource(context, "bezel"+ Math.abs(item.name.hashCode())%10));
		if (item.name.startsWith("(All")) iconView.setVisibility(View.INVISIBLE);
		else iconView.setVisibility(View.VISIBLE);

		return view;
	}

	public int getFlagResource(Context context, String name) {
		int resId = context.getResources().getIdentifier(name, "drawable", "com.chart");
		return resId;
	}

}

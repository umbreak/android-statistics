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
import com.chart.pojos.CategoryModel;
//Adapter for the AllContentsFragment
public class CategoriesAdapter extends ArrayAdapter<CategoryModel>{
	private final LayoutInflater mInflater;
	private final Context context;
	public List<CategoryModel> categories;

	public CategoriesAdapter(Context context) {
		super(context, R.layout.list_item_categories);

		this.context=context;
		mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}


	public void setData(List<CategoryModel> data) {
		categories=data;
		clear();
		if (data != null) {
			for (CategoryModel category : data) {
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
		CategoryModel item = getItem(position);

		((TextView) view.findViewById(android.R.id.text1)).setText(item.name);

		final ImageView iconView = (ImageView) view.findViewById(R.id.imageView1);
		iconView.setImageResource(getFlagResource(context, "bezel"+ item.id%10));
		if (item.id==0) iconView.setVisibility(View.INVISIBLE);
		else iconView.setVisibility(View.VISIBLE);
		return view;
	}

	public int getFlagResource(Context context, String name) {
		int resId = context.getResources().getIdentifier(name, "drawable", "com.chart");
		return resId;
	}

}

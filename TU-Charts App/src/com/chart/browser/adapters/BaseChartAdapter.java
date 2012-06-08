package com.chart.browser.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chart.AppUtils;
import com.chart.R;
import com.chart.pojos.BaseChartModel;
//Adapter for the AllContentsFragment
public class BaseChartAdapter extends ArrayAdapter<BaseChartModel>{
	
	private final LayoutInflater mInflater;
	private final boolean categoryVisibility;
	public BaseChartAdapter(Context context, boolean categoryVisibility) {
		super(context, R.layout.list_item_chart);
		this.categoryVisibility= categoryVisibility;
		mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}


	public void setData(List<BaseChartModel> data) {
		clear();
		if (data != null) {
			for (BaseChartModel category : data) {
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
			view = mInflater.inflate(R.layout.list_item_chart, parent, false);
		} else {
			view = convertView;
		}

		//Recover ChartEntry Data and fill the list_item_chart layout with it.
		BaseChartModel item = getItem(position);

		((TextView) view.findViewById(R.id.name)).setText(item.name);
		((TextView) view.findViewById(R.id.description)).setText(item.description);
		((TextView) view.findViewById(R.id.category)).setText(item.category.name);
		if (categoryVisibility)
			((RelativeLayout) view.findViewById(R.id.layoutCategory)).setVisibility(View.VISIBLE);
		else
			((RelativeLayout) view.findViewById(R.id.layoutCategory)).setVisibility(View.INVISIBLE);

		((TextView) view.findViewById(R.id.date)).setText(AppUtils.i.date_format.format(item.date));
		return view;
	}


}

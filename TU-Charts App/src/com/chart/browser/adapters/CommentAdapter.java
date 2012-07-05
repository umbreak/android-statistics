package com.chart.browser.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.chart.AppUtils;
import com.chart.R;
import com.chart.pojos.CommentModel;
//Adapter for the AllContentsFragment
public class CommentAdapter extends ArrayAdapter<CommentModel>{

	private final LayoutInflater mInflater;

	public CommentAdapter(Context context) {
		super(context, R.layout.list_comments);
		mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void setData(List<CommentModel> data) {
		clear();
		if (data != null) {
			for (CommentModel comment : data) {
				add(comment);
			}
		}
	}

	/**
	 * Populate new items in the list.
	 */
	@Override public View getView(int position, View convertView, ViewGroup parent) {
		View view;

		if (convertView == null) {
			view = mInflater.inflate(R.layout.list_comments, parent, false);
		} else {
			view = convertView;
		}

		//Recover ChartEntry Data and fill the list_item_chart layout with it.
		CommentModel item = getItem(position);

		if (item!= null){
			System.out.println("item=" + item);
			((TextView) view.findViewById(R.id.name)).setText(item.user.username);

			((TextView) view.findViewById(R.id.description)).setText(item.text);
			//		if (categoryVisibility)
			//			((RelativeLayout) view.findViewById(R.id.layoutCategory)).setVisibility(View.VISIBLE);
			//		else
			//			((RelativeLayout) view.findViewById(R.id.layoutCategory)).setVisibility(View.INVISIBLE);

			((TextView) view.findViewById(R.id.date)).setText(AppUtils.i.date_format.format(item.date));
		}
		return view;
	}


}

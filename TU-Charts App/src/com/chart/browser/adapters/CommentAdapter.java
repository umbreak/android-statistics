package com.chart.browser.adapters;

import static com.chart.AppUtils.EMAIL;
import java.util.List;
import android.content.Context;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.chart.AppUtils;
import com.chart.R;
import com.chart.callbacks.CustomOnClickListener;
import com.chart.callbacks.OnCustomClickListener;
import com.chart.pojos.CommentModel;
//Adapter for the AllContentsFragment
public class CommentAdapter extends ArrayAdapter<CommentModel>{

	private final LayoutInflater mInflater;
	private String myEmail;
	private OnCustomClickListener callback;

	public CommentAdapter(Context context, OnCustomClickListener callback) {
		super(context, R.layout.list_comments);
		mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		myEmail= PreferenceManager.getDefaultSharedPreferences(context).getString(EMAIL, "");
		this.callback=callback;
	}

	public void setData(List<CommentModel> data) {
		clear();
		if (data != null) {
			for (CommentModel comment : data) {
				add(comment);
			}
		}
	}

	private static class CommentView{
		public TextView name;
		public TextView description;
		public TextView date;
		public ImageButton delete;
	}
	@Override public View getView(int position, View convertView, ViewGroup parent) {
		CommentView holder;
		
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.list_comments, parent, false);
			holder = new CommentView();
			holder.name=((TextView) convertView.findViewById(R.id.name));
			holder.description=((TextView) convertView.findViewById(R.id.description));
			holder.date=((TextView) convertView.findViewById(R.id.date));
			holder.delete=((ImageButton) convertView.findViewById(R.id.delete));
			convertView.setTag(holder);
			convertView.setClickable(true);
			convertView.setFocusable(true);
		} else 
			holder = (CommentView)convertView.getTag();
		

		//Recover ChartEntry Data and fill the list_item_chart layout with it.
		CommentModel item = getItem(position);
		if (item!= null){
			holder.name.setText(item.user.username);
			holder.description.setText(item.text);
			holder.date.setText(AppUtils.i.date_format.format(item.date));
			if (myEmail.equals(item.user.email)){
				holder.delete.setVisibility(View.VISIBLE);
				holder.delete.setOnClickListener(new CustomOnClickListener(callback, item));
			}else
				holder.delete.setVisibility(View.GONE);

		}
		return convertView;
	}
}

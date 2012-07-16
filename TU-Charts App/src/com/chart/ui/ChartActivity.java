package com.chart.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.chart.R;
import com.chart.pojos.BaseChartModel;

public class ChartActivity extends SherlockFragmentActivity{
	private BaseChartModel chart;

	//	private Context aContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.simple_layout);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		chart = getIntent().getParcelableExtra("chart");

		setTitle("");
		Fragment f= getSupportFragmentManager().findFragmentByTag("Chart_" + chart.id);
		if (f == null){
			Bundle b = new Bundle();
			b.putParcelable("chart", chart);
			getSupportFragmentManager().beginTransaction()
			.replace(android.R.id.content, Fragment.instantiate(this, ChartFragment.class.getName(), b), "Chart_" + chart.id)
			.setCustomAnimations(R.anim.fragment_slide_left_enter,R.anim.fragment_slide_left_exit,R.anim.fragment_slide_right_enter,R.anim.fragment_slide_right_exit)
			.commit();
		}
	}

	@Override public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// app icon in action bar clicked; go home
			Intent intent = new Intent(this, HomeActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}

package com.chart.browser.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.SimpleAdapter;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.chart.HomeActivity;
import com.chart.R;
import com.chart.pojos.ChartEntry;

public class ChartActivity extends SherlockFragmentActivity{
	private ChartEntry chart;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.simple_layout);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		chart = getIntent().getParcelableExtra("chart");
		setTitle(chart.name);
		if (savedInstanceState == null){
			Bundle b = new Bundle();
			b.putParcelable("chart", chart);
			getSupportFragmentManager().beginTransaction()
			.add(android.R.id.content, Fragment.instantiate(this, ChartFragment.class.getName(), b), "Chart")
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
	public static class InfoFragment extends SherlockListFragment{
		private ChartEntry chart;
		private FragmentManager fm;

		public static InfoFragment newInstance(ChartEntry chart) {
			InfoFragment f = new InfoFragment();
			// Supply index input as an argument.
			Bundle args = new Bundle();
			args.putParcelable("chart", chart);
			f.setArguments(args);
			return f;
		}

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			chart = (ChartEntry) (getArguments() != null ? getArguments().getParcelable("chart") : null);
			fm = getActivity().getSupportFragmentManager();
		}

		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);

			// Give some text to display if there is no data.
			setEmptyText("No data for Chart found");

			// We have a menu item to show in action bar.
			setHasOptionsMenu(true);

			List<Map<String, String>> data = new ArrayList<Map<String,String>>();
			Map<String, String> datum = new HashMap<String, String>(2);

			datum.put("title", "Name");
			datum.put("date", chart.name);
			data.add(datum);

			datum = new HashMap<String, String>(2);
			datum.put("title", "Description");
			datum.put("date", chart.description);
			data.add(datum);
			
			datum = new HashMap<String, String>(2);
			datum.put("title", "Date of creation");
			datum.put("date", chart.date);
			data.add(datum);

			datum = new HashMap<String, String>(2);
			datum.put("title", "Category");
			datum.put("date", chart.category.name);
			data.add(datum);

			datum = new HashMap<String, String>(2);
			datum.put("title", "Num. Comments");
			datum.put("date", String.valueOf(chart.num_comments));
			data.add(datum);

			datum = new HashMap<String, String>(2);
			datum.put("title", "Num. votes");
			datum.put("date", String.valueOf(chart.votes));
			data.add(datum);
			SimpleAdapter adapter = new SimpleAdapter(getActivity(), data,android.R.layout.simple_list_item_2, new String[] {"title", "date"},
					new int[] {android.R.id.text1,android.R.id.text2});
			setListAdapter(adapter);

		}

		@Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
			// Place an action bar item for searching.
			menu.clear();
			MenuItem item = menu.add("Chart");
			item.setIcon(android.R.drawable.ic_menu_revert);
			item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		}

		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			Fragment frag=fm.findFragmentByTag(fm.getBackStackEntryAt
					(fm.getBackStackEntryCount()-1).getName());
			FragmentTransaction ft= fm.beginTransaction();
			ft.setCustomAnimations(R.anim.fragment_slide_left_enter,R.anim.fragment_slide_left_exit,R.anim.fragment_slide_right_enter,R.anim.fragment_slide_right_exit);
//			ft.remove(this);
//			ft.add(android.R.id.content, frag, "Chart").commit();
			ft.replace(android.R.id.content, frag, "Chart");
			fm.popBackStack("Chart", FragmentManager.POP_BACK_STACK_INCLUSIVE);
			return true;
		}

	}


}

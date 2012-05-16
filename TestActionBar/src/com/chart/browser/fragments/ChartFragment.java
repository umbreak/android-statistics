package com.chart.browser.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.chart.R;
import com.chart.pojos.ChartEntry;

public class ChartFragment extends SherlockFragment{
	private ChartEntry chart;
	private FragmentManager fm;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		chart = (ChartEntry) (getArguments() != null ? getArguments().getParcelable("chart") : null);
		fm = getActivity().getSupportFragmentManager();
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.hello_world, container, false);
		View tv = v.findViewById(R.id.text);
		((TextView)tv).setText("Fragment where it will be included the Chart: " + chart.name);
		return v;
	}

	@Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// Place an action bar item for searching.
		menu.clear();
		MenuItem item = menu.add("Info");
		item.setIcon(android.R.drawable.ic_menu_info_details);
		item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Fragment frag=ChartActivity.InfoFragment.newInstance(chart);
		FragmentTransaction ft= fm.beginTransaction();
		ft.setCustomAnimations(R.anim.fragment_slide_left_enter,R.anim.fragment_slide_left_exit,R.anim.fragment_slide_right_enter,R.anim.fragment_slide_right_exit);
		ft.remove(this);	
		
		ft.add(android.R.id.content, frag, "Info").addToBackStack("Chart").commit();
		return true;
	}
}

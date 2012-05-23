package com.chart.browser.fragments;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.achartengine.GraphicalView;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.chart.R;
import com.chart.pojos.ChartEntry;
import com.chart.pojos.Point;

public class ChartFragment extends SherlockFragment{
	private ChartEntry chart;
	private FragmentManager fm;
	private ChartExample chartBuilder;
	GraphicalView mChart;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		System.out.println("Original");
		super.onCreate(savedInstanceState);
		chart = (ChartEntry) (getArguments() != null ? getArguments().getParcelable("chart") : null);
		fm = getActivity().getSupportFragmentManager();
		chartBuilder =  new ChartExample();

		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.hello_world, container, false);
		LinearLayout layout= (LinearLayout) v.findViewById(R.id.chart);
		mChart= chartBuilder.getView(v.getContext(), getPoints(),chart);
		layout.addView(mChart, new LayoutParams(LayoutParams.FILL_PARENT,
		          LayoutParams.FILL_PARENT));
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
	public void onResume() {
		super.onResume();
		System.out.println("BEFORE");
		if (chartBuilder != null)
			mChart.repaint();
		System.out.println("AFTER");
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Fragment frag=ChartActivity.InfoFragment.newInstance(chart);
		FragmentTransaction ft= fm.beginTransaction();
		ft.setCustomAnimations(R.anim.fragment_slide_left_enter,R.anim.fragment_slide_left_exit,R.anim.fragment_slide_right_enter,R.anim.fragment_slide_right_exit);
		//		ft.remove(this);
		//		ft.add(android.R.id.content, frag, "Info");
		ft.replace(android.R.id.content, frag, "Info");
		ft.addToBackStack("Chart").commit();
		return true;
	}

	private List<Point> getPoints() {
		Calendar c = Calendar.getInstance();
		List<Point> results = new ArrayList<Point>();
		c.set(2012, 9, 1);
		results.add(new Point(c.getTime(), 8.8));
		c.clear();
		c.set(2012, 9, 8);
		results.add(new Point(c.getTime(), 9.0));
		c.clear();
		c.set(2012, 9, 15);
		results.add(new Point(c.getTime(), 10.0));
		c.clear();
		c.set(2012, 9, 22);
		results.add(new Point(c.getTime(), 9.5));
		c.clear();
		c.set(2012, 9, 23);
		results.add(new Point(c.getTime(), 11.0));
		c.clear();
		c.set(2012, 9, 25);
		results.add(new Point(c.getTime(), 10.8));
		c.clear();
		c.set(2012, 9, 27);
		results.add(new Point(c.getTime(), 12.5));
		c.clear();
		c.set(2012, 9, 28);
		results.add(new Point(c.getTime(), 6.5));
		c.clear();
		c.set(2012, 9, 29);
		results.add(new Point(c.getTime(), 9.5));
		return results;
	}

}
